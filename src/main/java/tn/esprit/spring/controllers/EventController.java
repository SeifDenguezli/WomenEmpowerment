package tn.esprit.spring.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;



import tn.esprit.spring.entities.Donation;
import tn.esprit.spring.entities.Event;
import tn.esprit.spring.service.event.EventService;


@RestController
@RequestMapping("/Event")
public class EventController {
	@Autowired
	EventService eventService;
	
	@GetMapping("/export")
	public void exprotToCsv(HttpServletResponse response) throws IOException {
	
		
		try {
			 
	        response.setContentType("text/csv");
	        response.setHeader("Content-Disposition", "attachment; filename=users.csv");
			
	        // write to csv file //
	        
	        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
	                
	 
	        String[]  headings = {"eventId","createdAt","description " , "eventName ", "eventType ", "link " , "place"};
	        
	        String[] pojoclassPropertyName =  {"eventId" ,"createdAt","description" , "eventName" , "eventType" , "link" , "place"};
	              
	 
	        csvWriter.writeHeader(headings);
	        
	        List<Event> EventList = eventService.Get_all_Event();

	        
	        if(null!=EventList && !EventList.isEmpty()){
	            for (Event event : EventList) {
	                csvWriter.write(event, pojoclassPropertyName);
	              }
	            }
	        csvWriter.close();

			}catch(Exception e) {
				e.printStackTrace();

			}
        System.out.println("csv report downloaded successfully...........");
    }
		
	
	
	
	@PostMapping("/add-Event")
	@ResponseBody
	public void addEvent(@RequestBody Event event) {
		eventService.addEvent(event);
	}
	
	@DeleteMapping(path="deleteEvent/{idUser}/{idEvent}")
	public void deletEvent(@PathVariable("idUser")Long idUser,@PathVariable("idEvent")Long idEvent) {
		eventService.deleteEvent(idUser, idEvent);
	}
	  
	@PostMapping(path = "joinEvent/{userid}/{idEvent}")
	public void joinEvent(@PathVariable("userid")Long userid,@PathVariable("idEvent")Long idEvent) {
		
		eventService.joinEvent(userid, idEvent);
		
	}
	@GetMapping("/Get-all-Event")
	public List<Event> Get_all_Event(){
		return eventService.Get_all_Event();
	}
	
	@GetMapping("/maxEventTransaction")
	public Long GetMaxEventTransaction(){
		return eventService.retrieveMaxEventTransactioned();
	}
	@GetMapping("/userAllDonation/{userid}")
	public Long getUSERDonationByID(@PathVariable("userid")Long userid){
		return eventService.findUserDonationsById(userid);
	}
	  
	  
	  
	
}
