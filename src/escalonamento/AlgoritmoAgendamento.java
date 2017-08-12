package escalonamento;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class AlgoritmoAgendamento {
	
    protected List<Processo> processos;
    protected List<Processo> processoQueue;
    protected Integer currentTime;
    protected Agendamento agendamento;
    protected Processo processoEmExecucao;
    protected Integer startTimeProcEmExec;
    
    protected PrintWriter writer;
    
    protected Scanner s;
    
    public AlgoritmoAgendamento(List<Processo> processos, String nome, Scanner s){
        processos = new ArrayList<Processo>(processos);
        processoQueue = new ArrayList<Processo>();
        currentTime = 0;
        agendamento = new Agendamento(nome);
        processoEmExecucao = null;
        startTimeProcEmExec = null;
        
        this.s = s;
        
        try {
			writer = new PrintWriter(nome+"-processos.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

        //clone inputProcesses
//        for(Process process : inputProcesses){
//            processes.add(process.clone());
//        }
    }

    public void preencherFilaProcesso(){
        for(Processo processo : processos){
            //Chegada de processos que não estão bloqueados
            if(processo.getTempoChegada().compareTo(currentTime) <= 0 && !(processo.isBloqueado(currentTime))
            		&& !processoQueue.contains(processo)){
                processoQueue.add(processo);
                continue;
            }
            //Processos que estavam bloqueados mas que agora estão prontos
            if(processo.getBloquearAte().compareTo(currentTime) <= 0 && !processoQueue.contains(processo)){
                processoQueue.add(processo);
            }
        }
    }
    
    public void preencherFilaProcessoComExtraQuantum(){
        for(Processo process : processos){
            //Chegada de processos que não estão bloqueados
            if(process.getTempoChegada().compareTo(currentTime) <= 0 && !(process.isBloqueado(currentTime))
            		&& !processoQueue.contains(process)){
                processoQueue.add(process);
                
                addExtraQuantum();
                continue;
            }
            //Processos que estavam bloqueados mas que agora estão prontos
            if(process.getBloquearAte().compareTo(currentTime) <= 0 && !processoQueue.contains(process)){
                processoQueue.add(process);
                
                addExtraQuantum();
            }
        }
    }

	private void addExtraQuantum() {
		if(processoEmExecucao != null){
			processoEmExecucao.addExtraQuantum();
		}
	}

    public void executarProcesso(Processo processo){
        processoEmExecucao = processo;
        startTimeProcEmExec = currentTime;
        processoEmExecucao.start(currentTime);
        processoQueue.remove(processo);
        currentTime++;
    }

    private int readProcess;
    protected void addProcesso() {
    	if(50 != processos.size() && s.hasNextLine()){
    		String line = s.nextLine();
    		String[] readResult = line.split(",");
            processos.add(new Processo(Integer.parseInt(readResult[0]), Integer.parseInt(readResult[1]), 
            							Integer.parseInt(readResult[2]), Integer.parseInt(readResult[3]), 
            							Integer.parseInt(readResult[4])));
            readProcess++;
            System.out.println("read one more "+readProcess);
    	}
	}
    
    public abstract Agendamento gerarAgendamentoProcesso();
}
