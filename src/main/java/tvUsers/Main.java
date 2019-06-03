package tvUsers;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        FilterTvUsers filterTvUsers = new FilterTvUsers(new MyFileWriter(), new MyHttpClient());
        filterTvUsers.outputTvUsers();
    }

}
