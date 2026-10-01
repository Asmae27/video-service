package ma.xproce.videoservice.mappers;


import ma.xproce.videoservice.dto.CreatorRequest;
import ma.xproce.videoservice.dto.VideoRequest;
import ma.xproce.videoservice.entities.Creator;
import ma.xproce.videoservice.entities.Video;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class VideoMapper {

    private final ModelMapper modelMapper;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public VideoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Creator toCreator(CreatorRequest request) {

        return modelMapper.map(
                request,
                Creator.class
        );
    }

    public Video toVideo(VideoRequest request) {

        Video video = modelMapper.map(
                request,
                Video.class
        );

        if (request.getDatePublication() != null) {

            video.setDatePublication(
                    LocalDate.parse(
                            request.getDatePublication(),
                            FORMATTER
                    )
            );
        }

        return video;
    }
}