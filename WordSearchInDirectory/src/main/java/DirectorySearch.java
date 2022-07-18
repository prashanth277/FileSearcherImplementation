import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class DirectorySearch implements Searcher {
    @Override
    public Set<SearchResult> search(String filePath, String searchString){
        Set<SearchResult> searchResultSet = new HashSet<>();
        File dir = new File(filePath);
        if (dir.isDirectory()) {
            String fileName = "";
            File[] files = dir.listFiles();
            FileSearcher fileSearcher = new FileSearcher();
            for (File file : files) {
                try {
                    if (file.isFile()) {
                        fileName = filePath + "/" + file.getName();
                        searchResultSet.addAll(fileSearcher.search(fileName, searchString));
                    }
                    else if(file.isDirectory()){
                        return search(fileName,searchString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return searchResultSet;
    }
}
