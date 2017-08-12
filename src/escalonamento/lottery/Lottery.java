package escalonamento.lottery;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import escalonamento.Agendamento;
import escalonamento.AgendamentoItem;
import escalonamento.AlgoritmoAgendamento;
import escalonamento.Processo;

public class Lottery extends AlgoritmoAgendamento {

    protected Integer quantum;

    public Lottery(List<Processo> processos, Integer quantum, Scanner s){
        super(processos, "LT", s);
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
                else if(currentTime-startTimeProcEmExec >= quantum){
                    Integer tempoRestante = processoEmExecucao.getTempoExecucao() - (currentTime-startTimeProcEmExec);
                    processoEmExecucao.setTempoExecucao(tempoRestante);
                    processoQueue.add(processoEmExecucao);
                    agendamento.add(new AgendamentoItem(processoEmExecucao.getId(), startTimeProcEmExec, currentTime, false));
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
            
            //Distribuir tickets
        	int maxTicket = 0;
        	for(Processo p: processoQueue){
        		p.getTickets().clear();
        		int totalTicket = (p.getPrioridade()+1)*10;
        		for(int i = 0; i < totalTicket; i++){
        			p.getTickets().add(maxTicket++);
        		}
        	}
            
            //Sortear ticket vencedor
            int winnerTicket = new Random().nextInt(maxTicket);

            //Encontrar processo com o ticket vencedor
            Processo winnerProcess = getProcessoVencedor(winnerTicket); 
            		
            executarProcesso(winnerProcess);
        }
        
        writer.close();
        
        agendamento.ajustarCPU(currentTime);

        return agendamento;
    }
    
    public Processo getProcessoVencedor(Integer winnerTicket){
    	for(Processo process: processoQueue){
    		for(Integer ticket: process.getTickets()){
    			if(ticket.equals(winnerTicket)){
    				return process;
    			}
    		}
    	}
    	throw new IllegalArgumentException("Ticket invalido");
    }
    
}
