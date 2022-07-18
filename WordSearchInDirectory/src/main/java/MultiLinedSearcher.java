import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class MultiLinedSearcher implements Searcher {

    @Override
    public Set<SearchResult> search(String filePath, String searchString)  {
        Set<SearchResult> searchResultSet = new HashSet<>();
        File file = new File(filePath);
        if (!file.isFile()) {
            return searchResultSet;
        }
        Reader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            int strLength = searchString.length(), i = 0;
            PossibleSearchResult[] possibleSearchResults = new PossibleSearchResult[strLength];
            int filledSize = 0;
            int linenumber = 1;
            int columnNumber = 0;
            while ((i = reader.read()) != -1) {
                char c = (char) i;
                if (i == 10) {
                    linenumber++;
                    columnNumber = 0;
                } else {
                    columnNumber++;
                }
                PossibleSearchResult possibleSearchResult = new PossibleSearchResult(c, linenumber, columnNumber);
                if (filledSize < strLength) {
                    possibleSearchResults[filledSize++] = possibleSearchResult;
                    continue;
                } else {
                    for (int j = 0; j < strLength - 1; j++) {
                        possibleSearchResults[j] = possibleSearchResults[j + 1];
                    }
                    possibleSearchResults[strLength - 1] = possibleSearchResult;
                }
                boolean flag = false;
                for (int k = 0; k < strLength; k++) {
                    if (possibleSearchResults[k].character == searchString.charAt(k)) {
                        flag = true;
                    } else {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    searchResultSet.add(new SearchResult(file.getName(), possibleSearchResults[0].lineNumber, possibleSearchResults[0].columnNumber));
                }
            }
        } catch (IOException e) {
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

    private static class PossibleSearchResult {
        char character;
        int lineNumber;
        int columnNumber;

        PossibleSearchResult(char character, int lineNumber, int columnNumber) {
            this.character = character;
            this.lineNumber = lineNumber;
            this.columnNumber = columnNumber;
        }
    }
}
