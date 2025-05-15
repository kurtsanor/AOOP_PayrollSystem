
package Model;

import java.util.List;


public interface RecordOperations {
    
    List <String []> getAllRecords();
    List <String []> search(String searchInput);
    
}
