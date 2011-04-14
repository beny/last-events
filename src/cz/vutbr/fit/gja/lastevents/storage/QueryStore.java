package cz.vutbr.fit.gja.lastevents.storage;

import com.google.appengine.api.datastore.Key;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class QueryStore 
{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
    private String keyword;

    @Persistent
    private int type;

    @Persistent
    private Date date;
    

    public QueryStore(String keyword, int type, Date date) 
    {
    	this.keyword = keyword;
    	this.type = type;        
        this.date = date;
    }

    
    public Key getKey() {return key;}
    public String getKeyword() {return keyword;}
    public int getType() {return type;}    
    public Date getDate() {return date;}
    
    public void setKeyword(String keyword) {this.keyword = keyword;}
    public void setType(int type) {this.type = type;}  
    public void setDate(Date date) {this.date = date;}
}
