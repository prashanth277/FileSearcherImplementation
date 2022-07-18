import java.io.IOException;
import java.util.Set;

public interface Searcher {
    Set<SearchResult> search(String filePath, String searchString);
}
