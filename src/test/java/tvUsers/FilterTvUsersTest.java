package tvUsers;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public class FilterTvUsersTest {

    @Mock
    private MyFileWriter fileWriter;

    @Mock
    private MyHttpClient httpClient;

    @Captor
    private ArgumentCaptor<Set<String>> namesCaptor;

    // class under test
    private FilterTvUsers filterTvUsers;

    private String over150Chars = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa " +
            "vitae tortor condimentum lacinia quis vel. Commodo sed egestas egestas " +
            "fringilla phasellus faucibus scelerisque eleifend donec. " +
            "Risus at ultrices mi tempus imperdiet nulla malesuada pellentesque. " +
            "Arcu dui vivamus arcu felis bibendum ut.";

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        filterTvUsers = new FilterTvUsers(fileWriter, httpClient);
    }

    // make sure there duplicates are handled.
    @Test
    public void allUsersAreUnique() {
        // Arrange
        List<Comment> comments = new ArrayList<>();
        Comment a = new Comment(
                "patrick",
                "pat.tv",
                over150Chars
        );
        Comment b = new Comment(
                "patrick",
                "pat.tv",
                over150Chars
        );
        comments.add(a);
        comments.add(b);

        // Act
        Set<String> tvUsers = filterTvUsers.getTvUsers(comments);

        // Assert
        Assert.assertEquals(tvUsers.size(), 1);
    }


    @Test
    public void outputTvUsers() throws IOException, InterruptedException {
        // Arrange
        String responseBody = "[\n" +
                "  {\n" +
                "    \"postId\": 1,\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"id labore ex et quam laborum\",\n" +
                "    \"email\": \"Eliseo@gardner.tv\",\n" +
                "    \"body\": \"laudantium enim quasi est quidem sdfsdfsdfsfdmagnam voluptate ipsam eos\\ntempora quo necessitatibus\\ndolor quam autem quasi\\nreiciendis et nam sapiente accusantium\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"postId\": 1,\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"quo vero reiciendis velit similique earum\",\n" +
                "    \"email\": \"Jayne_Kuhic@sydney.com\",\n" +
                "    \"body\": \"est natus enim nihil est dolore omnis voluptatem numquam\\net omnis occaecati quod ullam at\\nvoluptatem error expedita pariatur\\nnihil sint nostrum voluptatem reiciendis et\"\n" +
                "  }]";
        when(httpClient.getResponseBody(anyString())).thenReturn(responseBody);

        // Act
        filterTvUsers.outputTvUsers();

        // Assert
        verify(fileWriter).writeUsersToFile(namesCaptor.capture(), anyString());
        Set<String> names = namesCaptor.getValue();
        Assert.assertEquals(names.size(), 1);
        Assert.assertEquals(names.iterator().next(), "id labore ex et quam laborum");
    }

}
