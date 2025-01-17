package sg.edu.nus.iss.paf.workshop25;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Workshop25Application implements CommandLineRunner{

	@Autowired
	private MessagePoller messagePoller;

	public static void main(String[] args) {
		SpringApplication.run(Workshop25Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		messagePoller.start();
	}

}
