package escalonamento;

public class Agendamento {
	
    private String nome;
    
    private long totalProcessos;
    private long turnaroundTime;
    private long waitingTime;
    private long responseTime;
    
    private long cpuUtil;
    
    public Agendamento(String nome){
    	this.nome = nome;
    }
    
    public String getNome(){
    	return nome;
    }
    
    public void add(AgendamentoItem item){
//        this.schedule.add(item);
        if(item.isFinalizado()){
        	totalProcessos++;
        	turnaroundTime += item.endTime - item.tempoChegada;
        	waitingTime += item.endTime - (item.tempoChegada + item.tempoExecucao);
        	responseTime += item.startTime - item.tempoChegada;
        	cpuUtil += item.tempoExecucao;
        }
    }
    
    private float cpu;

	public float getCpu() {
		return cpu;
	}
	
    public void ajustarCPU(long ct){
    	long ocio = ct - cpuUtil;
    	cpu = (float)(ct - ocio)/ct;
    }

	public long getTotalProcessos() {
		return totalProcessos;
	}

	public void addTotal() {
		this.totalProcessos++;
	}

	public long getTurnaroundTime() {
		return turnaroundTime;
	}

	public long getWaitingTime() {
		return waitingTime;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public long getCpuUtil() {
		return cpuUtil;
	}

}
