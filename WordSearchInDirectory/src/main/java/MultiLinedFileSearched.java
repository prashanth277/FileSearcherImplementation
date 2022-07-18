import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiLinedFileSearched implements Searcher{
    @Override
    public Set<SearchResult> search(String filePath, String searchString) {
        Set<SearchResult> searchResultSet = new HashSet<>();
        File file = new File(filePath);
        if (!file.isFile()) {
            return searchResultSet;
        }
        Reader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            int strLength = searchString.length(), i = 0;
            List<Character> possibleResult = new ArrayList<>();
            int index = 0;
            int filledSize = 0;
            int lineNumber = 1;
            int columnNumber = 0;
            while ((i = reader.read()) != -1) {
                char c = (char) i;
                if (i == 10) {
                    lineNumber++;
                    index = 0;
                } else {
                    index++;
                }
                if(filledSize == strLength){
                    filledSize = 0;
                }
                if (searchString.charAt(filledSize) == c) {
                    if(filledSize == 0){
                        columnNumber = index;
                    }
                    filledSize++;
                    possibleResult.add(c);
                } else {
                    filledSize = 0;
                    if(filledSize == 0){
                        columnNumber = index;
                    }
                    possibleResult.clear();
                    if (searchString.charAt(filledSize) == c) {
                        filledSize++;
                        possibleResult.add(c);
                    }
                }
                if(possibleResult.size() == searchString.length()){
                    searchResultSet.add(new SearchResult(file.getName(),lineNumber,columnNumber));
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return searchResultSet;
    }
}
