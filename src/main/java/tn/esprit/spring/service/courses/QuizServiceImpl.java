package tn.esprit.spring.service.courses;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import tn.esprit.spring.entities.Answer;
import tn.esprit.spring.entities.Certificate;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Quiz;
import tn.esprit.spring.entities.QuizQuestion;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.repository.AnswerRepository;
import tn.esprit.spring.repository.CertificateRepository;
import tn.esprit.spring.repository.CourseRepository;
import tn.esprit.spring.repository.QuestionRepository;
import tn.esprit.spring.repository.QuizzRepository;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.serviceInterface.courses.QuizService;
@Service
public class QuizServiceImpl implements QuizService {
@Autowired
QuizzRepository quizzRepository;
@Autowired
QuestionRepository questionRepository;
@Autowired
AnswerRepository answerRepository;
@Autowired
UserRepository userRepository;
@Autowired
CourseRepository courseRepository;
@Autowired
CertificateRepository certificateRepository;
@Autowired
SanctionLearnerImpl sanctionLearnerImpl;
	@Override
	public void addQuestionToQuiz(QuizQuestion q, Long quizId) {
		questionRepository.save(q);
		Quiz quiz = quizzRepository.findById(quizId).get();
		Set<QuizQuestion> quest = new HashSet<>();
		quest.add(q);
		quiz.setQuestions(quest);
		quizzRepository.flush();
		

	}

	@Override
	public void addListQuestionsToQuiz(Set<QuizQuestion> questions, Long quizId) {
		questionRepository.saveAll(questions);
		Quiz quiz = quizzRepository.findById(quizId).get();
		quiz.getQuestions().addAll(questions);
		quizzRepository.save(quiz);
		

	}

	@Override
	public void removeQuestion(Long questionId,Long quizId) {
		Quiz q = quizzRepository.findById(quizId).get();
		QuizQuestion qq = questionRepository.findById(questionId).get();
		
		q.getQuestions().remove(qq);
		quizzRepository.flush();
		questionRepository.deleteById(questionId);
		
	}

	@Override
	public void editQuestion(QuizQuestion question,Long questionId) {
		QuizQuestion quest = questionRepository.findById(questionId).get();
		quest.setQuestion(question.getQuestion());
		quest.setScore(question.getScore());
		questionRepository.flush();	
	}

	@Override
	public void addAnswersToQuestion(Set<Answer> answers, Long questionId) {
		answerRepository.saveAll(answers);
		QuizQuestion qq = questionRepository.findById(questionId).get();
		qq.getAnswers().addAll(answers);
		questionRepository.save(qq);
	}

	@Override
	public void setCorrectAnswer( Long answerId) {
		
		Answer answer = answerRepository.findById(answerId).get();
		answer.setCorrect(true);
		answerRepository.flush();
	}

	@Override
	public Answer answerQuizQuestion(Long idUser,Long idAnswer) {
		User user = userRepository.findById(idUser).get();
		
		Answer answer = answerRepository.findById(idAnswer).get();
		user.getAnswers().add(answer);
		userRepository.flush();
		return answer;
		
		
	}

	@Override
	public int calculScore(Long idUser,Long idQuiz) {
		String usercred = idUser.toString();
		String quizcred = idQuiz.toString();
		int score=0;
		Quiz quiz = quizzRepository.findById(idQuiz).get();
		
		List<String> userCorrectAnswers = quizzRepository.getUserScore(idUser);
		System.err.println(userCorrectAnswers);
		for (String userAns : userCorrectAnswers) {
			
			String[] ans = userAns.split(",");
			if(ans[0].equals(usercred) && ans[4].equals(quizcred)){
				score = score + Integer.parseInt(ans[3]);
				
			}
			
			
		}
		return score;
	}

	@Override
	public int userCourseScore(Long idUser, Long idCourse) {
		Course course = courseRepository.findById(idCourse).get();
		int scoretot=0;
		Set<Quiz> quizes = course.getQuiz();
		for (Quiz quiz : quizes) {
			
			scoretot= scoretot + calculScore(idUser, quiz.getQuizId());
			
		}
		
		return scoretot;
	}

	@Override
	public int userPassed(Long idUser, Long idCourse) {
		
	    Date date = new Date();  
		int nbr=0;
		float mark = 0;
		User u = userRepository.findById(idUser).get();
		Course cours = courseRepository.findById(idCourse).get();
		Set<Quiz> quizes = cours.getQuiz();
		for (Quiz quiz : quizes) {
		Set<QuizQuestion> questions =	quiz.getQuestions();
		for (QuizQuestion question : questions) {
			nbr= nbr + question.getScore();
		}
		}
		
		mark= (nbr*70)/100;
		if(userCourseScore(idUser,idCourse)>=mark) {
			System.out.println("YOU PASSED THIS COURSE");
			Certificate c = certificateRepository.findByCourseAndByUserId(idCourse,idUser);
			
			c.setAquired(true);
			c.setObtainingDate(date);
			certificateRepository.flush();
			
		}
		else  {
			System.out.println("You failed the test");
		}
		
		return nbr;
	}
	
	public byte[] createCertificateQr(Long certificateId) throws IOException, InterruptedException {
		Certificate c = certificateRepository.findById(1L).get();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://qrcode3.p.rapidapi.com/qrcode/text"))
				.header("content-type", "application/json")
				.header("x-rapidapi-host", "qrcode3.p.rapidapi.com")
				.header("x-rapidapi-key", "79cdf7e6eamsh61c3485c380509ap1476ddjsn52b879c4370a")
				.method("POST", HttpRequest.BodyPublishers.ofString("{\r\n    \"data\": \"https://linqr.app\",\r\n    \"image\": {\r\n        \"uri\": \"icon://appstore\",\r\n        \"modules\": true\r\n    },\r\n    \"style\": {\r\n        \"module\": {\r\n            \"color\": \"black\",\r\n            \"shape\": \"default\"\r\n        },\r\n        \"inner_eye\": {\r\n            \"shape\": \"default\"\r\n        },\r\n        \"outer_eye\": {\r\n            \"shape\": \"default\"\r\n        },\r\n        \"background\": {}\r\n    },\r\n    \"size\": {\r\n        \"width\": 200,\r\n        \"quiet_zone\": 4,\r\n        \"error_correction\": \"M\"\r\n    },\r\n    \"output\": {\r\n        \"filename\": \"qrcode\",\r\n        \"format\": \"png\"\r\n    }\r\n}"))
				.build();
		HttpResponse<byte[]> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
		System.out.println(response.body());
	
       System.out.println(response.headers()) ;
		return response.body();
		/******************/
	

	}
	
	
	

}
