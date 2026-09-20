package io.ailurid.ancestrygraph;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlToMarkdownConverter {
    public static void main(String[] args) throws IOException {
        Path input = Path.of(args[0]);
        Path output = Path.of(args[1]);

        String html = Files.readString(input);
        String md = FlexmarkHtmlConverter.builder().build().convert(html);
        Files.writeString(output, md);
    }
}