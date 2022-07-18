import java.io.*;
import java.sql.Timestamp;
import java.util.*;

public class IndexSearcher implements Searcher {
    private List<IndexedWord> indexedWords;
    private Map<String, Long> filepathVsIndexedTime;

    IndexSearcher() {
        indexedWords = new ArrayList<>();
        filepathVsIndexedTime = new HashMap<>();
    }

    @Override
    public Set<SearchResult> search(String filePath, String searchString) {
        Set<SearchResult> searchResultSet;
        File file = new File(filePath);
        try {
            if (file.isFile()) {
                Long indexTime = filepathVsIndexedTime.get(file.getName());
                if (indexTime == null || file.lastModified() >= indexTime) {
                    buildIndex(file);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        searchResultSet = grep(file, searchString);
        return searchResultSet;
    }

    private Set<SearchResult> grep(File file, String searchWord) {
        Set<SearchResult> searchResultSet = new HashSet<>();
        for (IndexedWord indexedWord : indexedWords) {
            if (Objects.equals(indexedWord.word, searchWord)) {
                searchResultSet.add(new SearchResult(file.getName(), indexedWord.getRow(), indexedWord.getCol()));
            }
        }
        return searchResultSet;
    }

    private void buildIndex(File file) {
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(file));
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Long createdTime = timestamp.getTime();
            filepathVsIndexedTime.put(file.getName(), createdTime);
            String ln;
            int row = 0;
            while ((ln = fileReader.readLine()) != null) {
                String[] words = ln.split("\\W+");
                row++;
                int column = 0;
                for (String word : words) {
                    word = word.toLowerCase();
                    column++;
                    indexedWords.add(new IndexedWord(file.getName(), word, row, column));
                }
            }
        } catch (IOException e) {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException();
        }
    }

    private static class IndexedWord {
        String fileName;
        String word;
        int row;
        int col;

        public IndexedWord(String fileName, String word, int row, int col) {
            this.fileName = fileName;
            this.word = word;
            this.row = row;
            this.col = col;
        }

        public String getFileName() {
            return fileName;
        }

        public String getWord() {
            return word;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        @Override
        public String toString() {
            return "IndexedWords{" +
                    "fileName='" + fileName + '\'' +
                    ", word='" + word + '\'' +
                    ", row=" + row +
                    ", col=" + col +
                    '}';
        }
    }
}
