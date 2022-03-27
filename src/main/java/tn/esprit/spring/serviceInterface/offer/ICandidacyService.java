package tn.esprit.spring.serviceInterface.offer;

import java.util.List;

import tn.esprit.spring.entities.Candidacy;
import tn.esprit.spring.entities.Offer;
import javax.mail.MessagingException;

public interface ICandidacyService {
	
    public List<Candidacy> getAllCandidacies();
	
	public void postulerOffre (Candidacy candidacy,Long offerId,Long userId );
	
	public void SetFavorite(Long candidacy_id,boolean is_bookmarked);
	
    public List<String> getMyCandidacy(String keyword);
    
    public List<String> getMyFavoriteCandidacy(String keyword);

    public void HoldCandidacy(Long candidacy_id) throws MessagingException;
    
    public void RestrainCandidacy(Long candidacy_id) throws MessagingException;
   }
