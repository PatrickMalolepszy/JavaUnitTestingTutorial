package tvUsers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class MyFileWriter {

    public void writeUsersToFile(Set<String> tvUsers, String filename) throws IOException {
        String fileOutput = tvUsers.stream()
                .reduce((x,y) -> x + '\n' + y)
                .orElseThrow(RuntimeException::new);
        Files.writeString(Path.of(filename), fileOutput);
    }
}
