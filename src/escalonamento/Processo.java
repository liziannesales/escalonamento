package escalonamento;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Processo {

	public static final String METRICAS = "TurnaroundTime = %d \t waitingTime = %d \t responseTime = %d";
	
    private int id;
    private int tempoExecucao;
    private int duracao;
    private int tempoChegada;
    private int tempoBloqueio;
    private int prioridade;

    private Integer bloquearAte = null;
    
    private List<Integer> tickets;
    
    private int extraQuantum;
    
    public Processo(Integer id, Integer tempoChegada, Integer prioridade, 
    		Integer tempoExecucao, Integer tempoBloqueio){

    	this.id = id;
    	this.tempoExecucao = tempoExecucao;
    	this.duracao = tempoExecucao;
    	this.tempoChegada = tempoChegada;
    	this.tempoBloqueio = tempoBloqueio;
    	this.prioridade = prioridade;
    	
		this.tickets = new ArrayList<Integer>(0);

    	this.bloquearAte = -1;

    }

    public void setTempoExecucao(Integer tempoExecucao) {
        this.tempoExecucao = tempoExecucao;
    }

    public Integer getId() {
        return id;
    }

    public Integer getTempoExecucao() { 
    	return tempoExecucao; 
    }
    
    //TODO mudar para tempo restante
    public Integer getDuracao() { 
    	return duracao; 
    }

    public Integer getTempoChegada() {
        return tempoChegada;
    }

    public Integer getTempoBloqueio() {
        return tempoBloqueio;
    }

    public Processo clone(){ 
    	return new Processo(this.id, this.tempoChegada, this.prioridade,
    			 			this.tempoExecucao, this.tempoBloqueio);
    }

    public void start(Integer currentTime){
    	if(tempoBloqueio > 0){
    		bloquearAte = currentTime + tempoBloqueio + 1 ; //+1 por causa do bloqueio depois de 1ms
    	}
    }

    public Boolean isBloqueado(Integer currentTime){
        return (bloquearAte.compareTo(currentTime)) >= 0 ;
    }

    public Integer getBloquearAte(){
        return bloquearAte;
    }
    
    public Integer getPrioridade(){
    	return prioridade;
    }

    public List<Integer> getTickets(){
    	return tickets;
    }
    
    public void addExtraQuantum(){
    	extraQuantum++;
    }
    
    public int getExtraQuantum(){
    	return extraQuantum;
    }
    
    public void calcularMetricas(Integer startTime, Integer finishTime, PrintWriter writer){
    	//termino - chegada
        int turnaroundTime = finishTime - tempoChegada;
        //termino - (chegada + execução)
        int waitingTime = finishTime - (tempoChegada + tempoExecucao);
        //começo da execução - chegada
        int responseTime = startTime - tempoChegada;
        
        writer.print("idProcesso: " + getId() + "\t");
        writer.println(String.format(METRICAS, turnaroundTime, waitingTime, responseTime));
    }
    
	@Override
	public boolean equals(Object obj) {
		if (this.getClass().isInstance(obj)) {
			Processo outro = (Processo) obj;

			if (outro.getId() == null && this.getId() == null) {
				return super.equals(obj);
			}
			
			if (outro.getId() == null || this.getId() == null) {
				return false;
			}
			
			return outro.getId().equals(this.getId());
		}
		return false;
	}
    
}
