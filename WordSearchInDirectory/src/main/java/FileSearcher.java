import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSearcher implements Searcher {
    @Override
    public Set<SearchResult> search(String filePath, String searchString) {
        Set<SearchResult> searchResultSet = new HashSet<>();
        File file = new File(filePath);
        int index ;
        int line = 0;
        BufferedReader bufferedReader = null;
        try {
            if (file.isFile()) {
                 bufferedReader = new BufferedReader(new FileReader(file));
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    line++;
                    if (str.contains(searchString)) {
                        index = str.indexOf(searchString);
                        searchResultSet.add(new SearchResult(file.getName(), line, index));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return searchResultSet;
    }

}

