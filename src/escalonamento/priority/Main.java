package escalonamento.priority;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import escalonamento.Agendamento;
import escalonamento.AlgoritmoAgendamento;
import escalonamento.Processo;

public class Main {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String csvFile = "src/cenario1.txt";
        String line = null;
        String csvSplitBy = ",";
        ArrayList<Processo> processos = new ArrayList<>();
        
        int alfa = 50;
        Scanner s = null;
        try {
            
            s = new Scanner(new BufferedReader(new FileReader(csvFile)));
        	while(alfa != processos.size() && s.hasNextLine()){
        		line = s.nextLine();
        		String[] readResult = line.split(csvSplitBy);
                processos.add(new Processo(Integer.parseInt(readResult[0]), Integer.parseInt(readResult[1]), 
                							Integer.parseInt(readResult[2]), Integer.parseInt(readResult[3]), 
                							Integer.parseInt(readResult[4])));
        	}

        } catch(FileNotFoundException e){
            System.out.println("File not found");
            System.exit(1);
        } 
        
        AlgoritmoAgendamento algAgendamento = new Priority(processos, s);
        Agendamento agendamento = algAgendamento.gerarAgendamentoProcesso();
        System.out.println("Priority done");
        
        long stopTime = System.currentTimeMillis();
        
        discussion(stopTime - startTime, agendamento);
	}
	
	public static void discussion(long tempoSimulacao, Agendamento agendamento){
		long totalProcessos = agendamento.getTotalProcessos();
		float avgTurn = (float)agendamento.getTurnaroundTime() / totalProcessos;
		float avgRespose = (float)agendamento.getResponseTime() / totalProcessos;
		float avgWait = (float)agendamento.getWaitingTime() / totalProcessos;
		
		System.out.println(agendamento.getNome() + " - Tempo de retorno (turnaround) medio: " + avgTurn);
		System.out.println(agendamento.getNome() + " - Tempo de responta medio: " + avgRespose);
		System.out.println(agendamento.getNome() + " - Tempo de espera medio: " + avgWait);
		System.out.println(agendamento.getNome() + " - Duração da simulação(ms): " + tempoSimulacao);
		System.out.println(agendamento.getNome() + " - Utilização do processador: " + agendamento.getCpuUtil());
		System.out.println(agendamento.getNome() + " - Utilização do processador (calculo diferente): " + agendamento.getCpu());

		try {
			PrintWriter writer = new PrintWriter(agendamento.getNome()+"-resultado.txt");
			writer.println("Tempo de retorno (turnaround) medio: " + avgTurn);
			writer.println("Tempo de responta medio: " + avgRespose);
			writer.println("Tempo de espera medio: " + avgWait);
			writer.println("Duração da simulação(ms): " + tempoSimulacao);
			writer.println("Utilização do processador: " + agendamento.getCpuUtil());
			writer.println("Utilização do processador (calculo diferente): " + agendamento.getCpu());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
        }
    }
}
