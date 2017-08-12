package escalonamento;

public class AgendamentoItem {
	
    protected Integer idProcesso;
    protected Integer startTime;
    protected Integer endTime;
    
    protected Boolean finalizado;
    protected Integer tempoChegada;
    protected Integer tempoExecucao;

    public AgendamentoItem(Integer idProcesso, Integer startTime, Integer endTime, Boolean finalizado){
        this.idProcesso = idProcesso;
        this.startTime = startTime;
        this.endTime = endTime;
        this.finalizado = finalizado;
    }
    
    public AgendamentoItem(Integer idProcesso, Integer startTime, Integer endTime, Boolean finalizado, 
    					Integer tempoChegada, Integer tempoExecucao){
        this.idProcesso = idProcesso;
        this.startTime = startTime;
        this.endTime = endTime;
        this.finalizado = finalizado;
        this.tempoChegada = tempoChegada;
        this.tempoExecucao = tempoExecucao;
    }

    public Integer getIdProcesso() {
        return idProcesso;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public Boolean isFinalizado() {
        return finalizado;
    }
}
