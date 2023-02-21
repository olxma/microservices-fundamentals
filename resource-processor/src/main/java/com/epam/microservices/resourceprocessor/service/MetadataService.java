package com.epam.microservices.resourceprocessor.service;

import com.epam.microservices.resourceprocessor.exception.ExtractMetadataException;
import com.epam.microservices.resourceprocessor.mapper.AudioFileMetadataMapper;
import com.epam.microservices.resourceprocessor.model.AudioFileMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;

@Slf4j
@Service
public class MetadataService {

    public AudioFileMetadata extractMetadata(ByteArrayResource resource) {
        try (InputStream input = resource.getInputStream()) {
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);

            return AudioFileMetadataMapper.INSTANCE.toAudioFileMetadata(metadata);
        } catch (Exception e) {
            log.error("Couldn't extract metadata from resource", e);
            throw new ExtractMetadataException(e);
        }
    }
}
