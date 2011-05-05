package cz.vutbr.fit.gja.lastevents.logic;

import java.util.ArrayList;

import org.json.simple.JSONArray;

/**
 * Data class represents one search query of artists.
 * @author Petr Nohejl <xnohej00@stud.fit.vutbr.cz>
 */
public class QueryArtist
{
	private String keyword;
	private ArrayList<String> names;


	/**
	 * Create query.
	 */
	public QueryArtist()
	{
		this.names = new ArrayList<String>();
	}


	/**
	 * Create query.
	 */
	public QueryArtist(String keyword)
	{
		this.keyword = keyword;
		this.names = new ArrayList<String>();
	}


	/**
	 * Set keyword.
	 */
	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}


	/**
	 * Add artist name to query.
	 */
	public void addName(String name)
	{
		this.names.add(name);
	}


	/**
	 * Print query info to standard output.
	 */
	public void printQuery()
	{
		System.out.println("----------------------------------------");
		System.out.println("KEYWORD: " + keyword);
		for(int i=0;i<names.size();i++)
		{
			System.out.println("NAME #" + i + ": " + names.get(i));
		}
	}

	/**
	 * @return JSON data representation.
	 */
	@SuppressWarnings("unchecked")
	public String getJSONResult(){

		JSONArray namesJSON = new JSONArray();
		for(int i=0;i<names.size();i++){
			namesJSON.add(names.get(i));
		}

		return namesJSON.toJSONString();
	}

	public String getKeyword() { return this.keyword; }
	public ArrayList<String> getNames() { return this.names; }
}
