/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andreasdressel.hits;

import andreasdressel.hits.util.Node;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

/**
 *
 * @author Andreas Dressel
 */
public class HITS {
  
  private final HashMap<String, SortedSet<Integer>> invertedIndex;
  
  
  private HITS() {
    this.invertedIndex = new HashMap<String, SortedSet<Integer>>();
    
    
  }
  
  // how to handle multiple word queries? simply AND, or phrase queries?
  
  public synchronized Set<Node> computeHITS(String query, int iterations) {
    if(query == null) {
      System.err.println("Query must not be null. (computeHITS)");
      return null;
    }
    
    // this implementation is not case-sensitive
    query = query.toLowerCase();
    
    //@todo: handle stop words
    
    
    // 1. compute base set
    Set<Node> seedSet = new HashSet<Node>();
    getBaseSet(query, seedSet);
    
    // 2. extend base set to seed set
    extendToSeedSet(seedSet);
    
    // 3. iteratively calculate hits values, and normalize after each step
    double sumOfSquaredAuthScores = 0, sumOfSquaredHubScores = 0;
    while(iterations > 0) {
      
      for(Node node : seedSet) {
        sumOfSquaredAuthScores += Math.pow(node.calculateNewAuthScore(), 2);
        sumOfSquaredHubScores += Math.pow(node.calculateNewHubScore(), 2);
      }
      
      sumOfSquaredAuthScores = Math.sqrt(sumOfSquaredAuthScores);
      sumOfSquaredHubScores = Math.sqrt(sumOfSquaredHubScores);
      
      for(Node node : seedSet) {
        node.normalizeScores(sumOfSquaredAuthScores, sumOfSquaredHubScores);
      }
      
      iterations--;
      if(iterations > 0) {
        updateOldScores(seedSet);
      }
    }
    
    return seedSet;
  }
  
  
  private void getBaseSet(String query, Set<Node> set) {
    // base set has to contain all words from the query (AND)
    
    
  }
  
  private void extendToSeedSet(Set<Node> set) {
    
  }
  
  
  private void updateOldScores(Set<Node> set) {
    for(Node node : set) {
      node.updateOldAuthScore();
      node.updateOldHubScore();
    }
  }
}
