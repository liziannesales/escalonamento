package escalonamento.sdpm;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import escalonamento.Agendamento;
import escalonamento.AgendamentoItem;
import escalonamento.AlgoritmoAgendamento;
import escalonamento.Processo;

public class StateDependentPriorityMethod2 extends AlgoritmoAgendamento {

    protected Integer quantum;

    public StateDependentPriorityMethod2(List<Processo> processos, Integer quantum, Scanner s){
        super(processos, "SDPM2", s);
        this.quantum = quantum;
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

                //Verificar se o processo em execução terminou
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
                //Verificar se o processo em execução esta bloqueado
                else if(processoEmExecucao.isBloqueado(currentTime)){
                    agendamento.add(new AgendamentoItem(processoEmExecucao.getId(), startTimeProcEmExec, currentTime, false));
                    processoEmExecucao.setTempoExecucao(processoEmExecucao.getTempoExecucao() - ( currentTime-startTimeProcEmExec));
                    processoEmExecucao = null;
                    startTimeProcEmExec = null;
                }
                //Verificar se o processo em execução passou do tempo
                else if(currentTime-startTimeProcEmExec >= quantum + processoEmExecucao.getExtraQuantum()){
                    Integer remainingTime = processoEmExecucao.getTempoExecucao() - (currentTime-startTimeProcEmExec);
                    processoEmExecucao.setTempoExecucao(remainingTime);
                    processoQueue.add(processoEmExecucao);
                    agendamento.add(new AgendamentoItem(processoEmExecucao.getId(), startTimeProcEmExec, currentTime, false));
                    processoEmExecucao = null;
                    startTimeProcEmExec = null;
                }

            }

            preencherFilaProcessoComExtraQuantum();

            //Se não existe processo pronto
            if (processoQueue.isEmpty()){
                currentTime++;
                continue;
            }

            if(processoEmExecucao != null){
                currentTime++;
                continue;
            }

            executarProcesso(processoQueue.get(0));

        }

        writer.close();
        
        agendamento.ajustarCPU(currentTime);

        return agendamento;

    }
}
