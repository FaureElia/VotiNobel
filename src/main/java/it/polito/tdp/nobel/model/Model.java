package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> allEsami;
	private Set<Esame> migliore; // utilizzo un set perchè gli elementi non si possono ripetere
	private double mediaMigliore;
	
	// inizializzo un modello inserendo tutti gli esami in una lista!
	
	public Model() {
		EsameDAO esameDAO=new EsameDAO();
		this.allEsami=esameDAO.getTuttiEsami();
	}
	
	// creo una funzione che inizializza la ricorsiva! NON è la ricorsiva stessa!!
	// questa funzione restituisce un set (insieme di elementi univoci non ordinati) che rpresenta gli esami scelti
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
		
		/**IN QUESTO CASO IL LIVELLO L HA UN SIGNIFICATO DIFFERENTE!NON RAPPRESENTA QUANTI ESAMI HO MESSO ALL'INTERNO DELLA
		 * DELLA SOLUSIONE, MA RAPPRESENTA IL PROSSIMO ESAME SU CUI FARE IL TENTATIVO!
		 * IN POCHE PAROLE PRIMA L'ALGORITMO POSIZIONAVA GLI ESAMI CON ORDINI DIFFERENTI E POI SI FERMAVA QUANDO
		 * AVEVA RAGGIUNTO IL NUMERO LIMITE DI CREDITI. ADESSO INVECE L'ALGORIMO ESAMINA OGNI ESAME NELLO STESSO 
		 * ORDINE, TUTTAVIA DECIDE SE PRENDERLO O NON PRENDERLO. CIO' SIGNIFICA CHE (AD ESEMPIO) L'ULTIMO ESAME DELLA LISTA ALLESAMI
		 * SARA' PRESO SE MOLTI ESAMI PRECEDENTI (IN ORDINAMENTO) VERRANNO SCARTATI! 
		 */
		
		
		
		
		
		
		
	}
	
	//è il primo metodo che abbiamo creato,obsoleto perchè non considera l'eventualità di combinazioni doppie (conordine di inserimento diverso)
	
	/**
	 * NOTA: IL VERO PROBLEMA DI QUESTO METODO (cerca()) E' CHE SI PROVA A PRENDERE UN'ELEMENTO E RICHIAMARE LA RICORSIVA
	 * CONSIDERANDO TUTTI GLI ALTRI ELEMENTI! QUINDI PARTIRO' PRENDENDO ELEMENTO 1 E NELLA RICORSIVA PRENDERO' 2,3, ....
	 * TUTTAVIA AL SECONDO STEP DEL CICLO FOR A LIVELLO PIù ALTO PROVERO' A PRENDERE ELEMENTO 2 E RICHIAMARE LA RICORSIVA,
	 * SUCCEDERA' PERO' CHE NEL CICLO FOR DEL SECONDO LIVELLO DI RICORSIVA PROVERA' A PRENDERE IL PRIMO ELEMENTO CHE NON E' PRESENTE 
	 * NEL SET: ELEMENTO 1. OTTENGO QUINDI UNA COMBINAZIONI (2,1,3,4,5,6) che è apparentemente diversa da (1,2,3,4,5,6) ma produce lo stesso risultato
	 * @param parziale
	 * @param L
	 * @param numeroCrediti
	 */
	
	
	
	
	
	
	private void cerca(Set<Esame> parziale, int L, int numeroCrediti ) {
		int sommaCrediti=this.sommaCrediti(parziale); //tutti i crediti utilizzati finora
		if (sommaCrediti>numeroCrediti) 
			return ; // in questo caso la soluzione non è possibile!
		
		if (sommaCrediti==numeroCrediti) { //potrei avere una soluzione! (potrei essere al termine dell'algoritmo!)
			double mediaVoti=calcolaMedia(parziale);
			if (mediaVoti>mediaMigliore) {
				mediaMigliore=mediaVoti;
				migliore=new HashSet<>(parziale); // devo fare la copia perchè se modificherò parziale sarà modificato anche migliore
			}
			return;// anche in questo caso ho terminato, non devo richiamare l'algoritmo ricorsivo!
		}
		if (L==allEsami.size()) // ho finito i livelli perchè ho preso tutti gli esami possibili!
			return;	//NOTA: L rappresenta il livello che sto esplorando, ogni volta che entro nel ciclo for successivo, L si incrementa!
		// anche se non prendo l'esame, l'ho già esplorato!! questo significa che quando L sarà uguale al numero di esami(per il fatto che parte da 0)
		// avrò considerato tutte le prove ! mi dovrò fermare
		
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
