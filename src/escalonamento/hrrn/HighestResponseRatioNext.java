package escalonamento.hrrn;
import java.util.List;
import java.util.Scanner;

import escalonamento.Processo;
import escalonamento.Agendamento;
import escalonamento.AgendamentoItem;
import escalonamento.AlgoritmoAgendamento;

public class HighestResponseRatioNext extends AlgoritmoAgendamento{

    public HighestResponseRatioNext(List<Processo> processos, Scanner s){
        super(processos, "HRRN", s);
    }

    public  Agendamento gerarAgendamentoProcesso() {
        while(!processos.isEmpty()) {
        	
            //Verificar se o processo em execução terminou
            if(processoEmExecucao != null){
                if(processoEmExecucao.getTempoExecucao() == currentTime-startTimeProcEmExec){
                    agendamento.add(new AgendamentoItem(processoEmExecucao.getId(), 
                    				startTimeProcEmExec, currentTime, true,
                    				processoEmExecucao.getTempoChegada(),
                    				processoEmExecucao.getDuracao()));
                    
                    processos.remove(processoEmExecucao);
                    
                    processoEmExecucao.calcularMetricas(startTimeProcEmExec, currentTime, writer);

                    processoEmExecucao = null;
                    startTimeProcEmExec = null;
                    
                    addProcesso();
                }
            }

            //Verificar se o processo em execução esta bloqueado
            if(processoEmExecucao != null){
                if(processoEmExecucao.isBloqueado(currentTime)){
                    agendamento.add(new AgendamentoItem(processoEmExecucao.getId(), startTimeProcEmExec, currentTime, false));
                    processoEmExecucao.setTempoExecucao(processoEmExecucao.getTempoExecucao() - ( currentTime-startTimeProcEmExec));
                    processoEmExecucao = null;
                    startTimeProcEmExec = null;
                }
            }

            preencherFilaProcesso();

            //Se não existe processo pronto
            if (processoQueue.isEmpty()){
                currentTime++;
                continue;
            }

            if(processoEmExecucao != null){
                currentTime++;
                continue;
            }

            //Encontrar processos com o highest response ratio
            Processo processoHRR = getProcessoHighestResponseRatio();

            //run that process
            executarProcesso(processoHRR);
        }
        
        writer.close();
        
        agendamento.ajustarCPU(currentTime);
        
        return agendamento;
    }
    
	public  Processo getProcessoHighestResponseRatio(){
        Processo processo = processoQueue.get(0);
        Float highestResponseRatio = getResponseRatio(currentTime, processo);
        for(Processo p : processoQueue){
            if (getResponseRatio(currentTime, p) > highestResponseRatio){
                processo = p;
                highestResponseRatio = getResponseRatio(currentTime, p);
            }
        }
        return processo;
    }

    public  Float getResponseRatio(Integer currentTime, Processo processo){
        return (Float.valueOf((currentTime - processo.getTempoChegada()) + processo.getTempoExecucao()) / Float.valueOf(processo.getTempoExecucao()));
    }


}
