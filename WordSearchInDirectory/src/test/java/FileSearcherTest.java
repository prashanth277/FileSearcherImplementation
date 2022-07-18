import org.junit.Assert;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class FileSearcherTest {

    @Test
    public void testFile()  {
        Searcher fileSearcher = new FileSearcher();
        String path = "/home/prashanthm_500282/Downloads/sample3.txt";
        Set<Integer> actualResult = new HashSet<>();
        Set<SearchResult> searchResults = new HashSet<>();
        searchResults =  fileSearcher.search(path,"est");
        Set<Integer> result = new HashSet<>();
        result.add(7);
        result.add(17);
        result.add(15);
        for(SearchResult i:searchResults ){
            actualResult.add(i.getLineNumber());
        }
        assertEquals(result,actualResult);
    }

    @Test
    public void testDirectory()  {
        DirectorySearch directorySearch = new DirectorySearch();
        Set<SearchResult> actualResult;
        actualResult = directorySearch.search("/home/prashanthm_500282/Downloads","est");
        assertTrue(actualResult.contains(new SearchResult("sample3.txt",17,4)));

    }
    @Test
    public void testIndexSearch() {
        IndexSearcher indexSearcher = new IndexSearcher();
        Set<SearchResult> searchResultSet1 = indexSearcher.search("/home/prashanthm_500282/Downloads/sample3.txt","est");
        Set<SearchResult> searchResultSet2 = indexSearcher.search("/home/prashanthm_500282/Downloads/sample3.txt","erum");
        assertEquals(4,searchResultSet1.size());
        assertEquals(1,searchResultSet2.size());
    }

    @Test
    public void testMultiLineString()  {
        Set<SearchResult> actualResult = new HashSet<>();
        Searcher searcher = new MultiLinedSearcher();
        String str = "Mi\n" +
                "\n" +
                "Iam";
        Set<SearchResult> searchResults  = searcher.search("/home/prashanthm_500282/Downloads/sample4.txt",str);
        Set<SearchResult> searchResults2  = searcher.search("/home/prashanthm_500282/Downloads/sample4.txt","tata");
        System.out.println(searchResults2);
        Assert.assertEquals(1, searchResults.size());
    }
    @Test
    public void testMultiLineFileString()  {
        Searcher searcher = new MultiLinedFileSearched();
        String str = "prim";
        Set<SearchResult> searchResults2  = searcher.search("/home/prashanthm_500282/Downloads/sample3.txt",str);
        Assert.assertEquals(3, searchResults2.size());
        Assert.assertTrue(searchResults2.contains(new SearchResult("sample3.txt", 18, 38)));
    }

    @Test
    public void testPCProblem()  {
        Searcher searcher = new PCProblemWithBlockingQueue();
        String str = "prim";
        Set<SearchResult> searchResults2  = searcher.search("/home/prashanthm_500282/Downloads",str);
        Assert.assertTrue(searchResults2.contains(new SearchResult("sample3.txt", 18, 37)));
    }
}