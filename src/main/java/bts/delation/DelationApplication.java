package bts.delation;

import io.grpc.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class DelationApplication {

	public static void main(String[] args) throws IOException, InterruptedException {

		ConfigurableApplicationContext context = SpringApplication.run(DelationApplication.class, args);

		// Start the gRPC server
		Server grpcServer = context.getBean(Server.class);
		grpcServer.start();

		// Keep the main thread alive to allow gRPC server to keep running
		grpcServer.awaitTermination();
	}

}
