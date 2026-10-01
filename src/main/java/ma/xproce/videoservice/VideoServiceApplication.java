package ma.xproce.videoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ma.xproce.videoservice.entities.Creator;
import ma.xproce.videoservice.entities.Video;
import ma.xproce.videoservice.repositories.CreatorRepository;
import ma.xproce.videoservice.repositories.VideoRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class VideoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(
            CreatorRepository creatorRepository,
            VideoRepository videoRepository) {

        return args -> {

            // Création des creators
            Creator creator1 = Creator.builder()
                    .name("Badr")
                    .email("badr@gmail.com")
                    .build();

            Creator creator2 = Creator.builder()
                    .name("Asmae")
                    .email("asmae@gmail.com")
                    .build();

            Creator creator3 = Creator.builder()
                    .name("John")
                    .email("john@gmail.com")
                    .build();

            List<Creator> creators = List.of(
                    creator1,
                    creator2,
                    creator3
            );

            creatorRepository.saveAll(creators);

            // Création des vidéos
            Video video1 = Video.builder()
                    .name("Introduction à GraphQL")
                    .url("https://example.com/graphql")
                    .description("Introduction à GraphQL")
                    .datePublication(LocalDate.of(2026, 9, 1))
                    .creator(creator1)
                    .build();

            Video video2 = Video.builder()
                    .name("Spring Boot")
                    .url("https://example.com/spring")
                    .description("Cours Spring Boot")
                    .datePublication(LocalDate.of(2026, 9, 5))
                    .creator(creator2)
                    .build();

            Video video3 = Video.builder()
                    .name("Java avancé")
                    .url("https://example.com/java")
                    .description("Cours Java avancé")
                    .datePublication(LocalDate.of(2026, 9, 10))
                    .creator(creator1)
                    .build();

            videoRepository.saveAll(
                    List.of(video1, video2, video3)
            );
        };
    }
}

