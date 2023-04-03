package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> allEsami;
	private Set<Esame> migliore; // utilizzo un set perchè gli elementi non si possono ripetere
	private double mediaMigliore;
	
	public Model() {
		EsameDAO esameDAO=new EsameDAO();
		this.allEsami=esameDAO.getTuttiEsami();
	}
	
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		
		migliore =new HashSet<>(); // ogni volta che chiamo questo metodo sono sicuro che la variabile è vuota!
		mediaMigliore=0.0;
		
		Set <Esame> parziale =new HashSet<>();
		
		cercaMeglio(parziale, 0, numeroCrediti);
		return migliore;	
	}
	
	private void cercaMeglio(Set<Esame> parziale, int L, int numeroCrediti){
		
		int sommaCrediti=this.sommaCrediti(parziale);
		if (sommaCrediti>numeroCrediti) 
			return ;
		
		if (sommaCrediti==numeroCrediti) {//potrei avere una soluzione!
			double mediaVoti=calcolaMedia(parziale);
			if (mediaVoti>mediaMigliore) {
				mediaMigliore=mediaVoti;
				migliore=new HashSet<>(parziale); // devo fare la copia perchè se modificherò parziale sarà modificato anche migliore
			}
			return;
		}
		if (L==allEsami.size()) // ho finito i livelli perchè ho preso tutti gli esami possibili!
			return;	
		
		//vogliamo poter scegliere se aggiungere o non aggiungere un elemento della lista
		
		//provo ad aggiungere il prossimo elemento
		//L=0 scelgo se mettere esame 1 oppure non metterlo.     {e1}
		//L=2 scelgo se aggiungere esame n oppure non aggiungerlo! {e1,e2},{e1},{-}
		// in questo modo scorro gli esami solo una volta!
		
		
		parziale.add(allEsami.get(L));
		this.cercaMeglio(parziale, L+1, numeroCrediti);
		parziale.remove(allEsami.get(L));//IMPORTANTISSIMO IL BACKTRACKING!! RICHIAMO PIù VOLTE LA FUNZIONE RICORSIVA IN UNA STESSA ZONA
		
		// provo a non aggiungere il prossimo elemento
		
		this.cercaMeglio(parziale, L+1, numeroCrediti);
		
		
		
		
		
		
	}
	
	private void cerca(Set<Esame> parziale, int L, int numeroCrediti ) {
		int sommaCrediti=this.sommaCrediti(parziale);
		if (sommaCrediti>numeroCrediti) 
			return ;
		
		if (sommaCrediti==numeroCrediti) {//potrei avere una soluzione!
			double mediaVoti=calcolaMedia(parziale);
			if (mediaVoti>mediaMigliore) {
				mediaMigliore=mediaVoti;
				migliore=new HashSet<>(parziale); // devo fare la copia perchè se modificherò parziale sarà modificato anche migliore
			}
			return;
		}
		if (L==allEsami.size()) // ho finito i livelli perchè ho preso tutti gli esami possibili!
			return;	
		
		//se arrivo qui il numero di crediti> sommaCrediti
		for (Esame e:allEsami) {
			
			if (!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale,L+1,numeroCrediti);
				parziale.remove(e);//backtracking!
				}
			
			}
		
		
		
		}
	
	
	
	
	
	
	

	
	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
