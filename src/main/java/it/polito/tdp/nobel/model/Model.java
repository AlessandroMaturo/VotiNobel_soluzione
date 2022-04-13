package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> esami;
	private Set<Esame> migliore;
	private double mediaMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO(); //qua ricordati di inizializzare sempre la classe DAO
		this.esami = dao.getTuttiEsami(); //qua prende tutti gli esami dal database
	}
	
	//questo è il metodo da cui si parte
	public Set<Esame> calcolaSottoinsiemeEsami(int m) {
		//ripristino soluzione migliore
		migliore = new HashSet<Esame>();
		mediaMigliore = 0.0;
		
		Set<Esame> parziale = new HashSet<Esame>();
		//cerca1(parziale,0,m);
		cerca2(parziale,0,m);

		return migliore;	
	}

	private void cerca2(Set<Esame> parziale, int L, int m) {
		// Controllare i casi terminali
		int sommaCrediti = sommaCrediti(parziale);
				
		if(sommaCrediti > m) //soluzione non valida!
			return;
				
		if(sommaCrediti == m) {
			//soluzione valida! controlliamo se è la migliore (fino a qui)
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				migliore = new HashSet<Esame>(parziale);
				mediaMigliore = mediaVoti;
			}
					
			return;
		}
				
		//sicuramente, crediti < m
		if(L == esami.size())
			return;
		
		//provo ad aggiungere esami[L]
		parziale.add(esami.get(L));
		cerca2(parziale,L+1,m);
		
		//provo a "non aggiungere" esami[L]
		parziale.remove(esami.get(L));
		cerca2(parziale,L+1,m);
		
	}

	/*
	 * COMPLESSITA' N!
	 */
	private void cerca1(Set<Esame> parziale, int L, int m) {
		
		// Controllare i casi terminali
		int sommaCrediti = sommaCrediti(parziale);
		
		if(sommaCrediti > m) //soluzione non valida!
			return;
		
		if(sommaCrediti == m) {
			//soluzione valida! controlliamo se è la migliore (fino a qui)
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				migliore = new HashSet<Esame>(parziale);
				mediaMigliore = mediaVoti;
			}
			
			return;
		}
		
		//sicuramente, crediti < m
		if(L == esami.size()) 
			/*questo ti prende anche il caso mettessi un numero 
			di credito troppo alto che prendi tuttti gli esami e non arrivi a quel numero di crediti*/
			return;
		
		//generiamo i sotto-problemi, se sei arrivato a questo punto vuol dire che non è uscito prima
		for(Esame e : esami) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale, L+1, m);
				parziale.remove(e); //backtrakking  avendo usato HashSet puoi farlo così
			}
		}
		
	}

	// questo metodo te lo davano già
	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	// questo metodo te lo davano già
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
