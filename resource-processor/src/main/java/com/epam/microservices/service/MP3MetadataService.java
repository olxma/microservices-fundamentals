package com.epam.microservices.service;

import com.epam.microservices.mapper.AudioFileMetadataMapper;
import com.epam.microservices.model.AudioFileMetadata;
import com.epam.microservices.model.MetadataProperty;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class MP3MetadataService {

    public AudioFileMetadata extractMetadata(File file) {
        try (InputStream input = new FileInputStream(file)) {
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);

            for (MetadataProperty property : MetadataProperty.values()) {
                System.out.printf("%s: %s%n", property.getName(), metadata.get(property.getTag()));
            }
            return AudioFileMetadataMapper.INSTANCE.toAudioFileMetadata(metadata);
        } catch (Exception e) {
            e.printStackTrace(); // TODO: 31.01.2023 replace with custom exception throwing
        }
        return null; // TODO: 31.01.2023 remove it after adding custom exception throwing
    }
}
