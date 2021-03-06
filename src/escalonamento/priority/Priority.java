package escalonamento.priority;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import escalonamento.Agendamento;
import escalonamento.AgendamentoItem;
import escalonamento.AlgoritmoAgendamento;
import escalonamento.Processo;

public class Priority extends AlgoritmoAgendamento{

    public Priority(List<Processo> processos, Scanner s){
        super(processos, "P", s);
    }

    public  Agendamento gerarAgendamentoProcesso() {
        //Ordenar processo por tempo de chegada
        Collections.sort(processos, new Comparator<Processo>() {
            @Override
            public int compare(Processo p1, Processo p2) {
                return p1.getTempoChegada().compareTo(p2.getTempoChegada());
            }
        });


        while(!processos.isEmpty()){

            if(processoEmExecucao != null){

                //Verificar se o processo em execu��o terminou
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
                //Verificar se o processo em execu��o esta bloqueado
                else if(processoEmExecucao.isBloqueado(currentTime)){
                    agendamento.add(new AgendamentoItem(processoEmExecucao.getId(), startTimeProcEmExec, currentTime, false));
                    processoEmExecucao.setTempoExecucao(processoEmExecucao.getTempoExecucao() - ( currentTime-startTimeProcEmExec));
                    processoEmExecucao = null;
                    startTimeProcEmExec = null;
                }

            }

            preencherFilaProcesso();

            //Se n�o existe processo pronto
            if (processoQueue.isEmpty()){
                currentTime++;
                continue;
            }

            if(processoEmExecucao != null){
                currentTime++;
                continue;
            }

            Processo processoPrioritario = getProcessoComPrioridade();
            
            executarProcesso(processoPrioritario);

        }

        writer.close();
        
        agendamento.ajustarCPU(currentTime);

        return agendamento;

    }

    public Processo getProcessoComPrioridade(){
    	//Ordernar processos pela prioridade
        Collections.sort(processoQueue, new Comparator<Processo>() {
            @Override
            public int compare(Processo p1, Processo p2) {
                return p1.getPrioridade().compareTo(p2.getPrioridade());
            }
        });
    	
        return processoQueue.get(0);
    }

}
