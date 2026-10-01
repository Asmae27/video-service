package ma.xproce.videoservice.web;

import ma.xproce.videoservice.dto.CreatorRequest;
import ma.xproce.videoservice.dto.VideoRequest;
import ma.xproce.videoservice.entities.Creator;
import ma.xproce.videoservice.entities.Video;
import ma.xproce.videoservice.managers.CreatorManager;
import ma.xproce.videoservice.managers.VideoManager;
import ma.xproce.videoservice.repositories.VideoRepository;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Controller
public class VideoGraphQlController {

    private final CreatorManager creatorManager;
    private final VideoManager videoManager;
    private final VideoRepository videoRepository;

    public VideoGraphQlController(
            CreatorManager creatorManager,
            VideoManager videoManager,
            VideoRepository videoRepository) {

        this.creatorManager = creatorManager;
        this.videoManager = videoManager;
        this.videoRepository = videoRepository;
    }


    @QueryMapping
    public List<Video> videoList() {

        return videoRepository.findAll();
    }

    @QueryMapping
    public Creator creatorById(@Argument Long id) {

        return creatorManager.findById(id);
    }


    @MutationMapping
    public Creator saveCreator(
            @Argument CreatorRequest creator) {

        return creatorManager.saveCreator(creator);
    }

    @MutationMapping
    public Video saveVideo(
            @Argument VideoRequest video) {

        Creator creator =
                creatorManager.saveCreator(
                        video.getCreator()
                );

        return videoManager.saveVideo(
                video,
                creator
        );
    }


    @SubscriptionMapping
    public Flux<Video> notifyVideoChange() {

        return Flux.interval(Duration.ofSeconds(1))
                .map(counter -> {

                    CreatorRequest creatorRequest =
                            CreatorRequest.builder()
                                    .name("Creator-" + counter)
                                    .email("x@gmail.com")
                                    .build();

                    Creator creator =
                            creatorManager.saveCreator(
                                    creatorRequest
                            );

                    Video video =
                            videoManager.findById(1L);

                    video.setCreator(creator);

                    return videoManager.updateVideo(video);
                });
    }
}