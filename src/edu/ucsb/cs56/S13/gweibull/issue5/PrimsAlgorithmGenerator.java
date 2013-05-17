package edu.ucsb.cs56.S13.gweibull.issue5;
import java.util.ArrayList;
import java.util.Random;

/** An implementation of Prim's algorithm for Maze creation. A starting place 
	is chosen at random and cleared, and the surrounding 4 neighbors are added to the 
	frontier list. While not all cells are cleared, a place in the frontier list is chosen
	at random and it's neighbors are added to the frontier. Every time a cell is cleared, 
	the wall to its cleared neighbor is removed, and in the (common) case of more than one
	cleared neighbors, a wall towards a randomly chosen of those neighbors is chosen. 
 
    @author Gunnar Weibull
    @version MazeGame for CS56, Spring 2013
    @see MazeGrid
*/
public class PrimsAlgorithmGenerator extends MazeGenerator {
	private ArrayList<Cell> frontier;
	private Random random;
	private boolean showMarkers;
	
	public PrimsAlgorithmGenerator(MazeGrid grid){
		this.grid = grid;
		this.random = new Random();
		this.showMarkers = false;
		
		// choose a random point in this grid to start at
		
		int row = (int)(Math.random()*grid.getRows());
		int col = (int)(Math.random()*grid.getCols());
		/**
		int row = 0;
		int col = 0;
		*/
		this.frontier = new ArrayList<Cell>();
		Cell firstCell = new Cell(row,col);
		grid.carvePath(firstCell,findNeighbors(firstCell).get(random.nextInt(findNeighbors(firstCell).size())));
		if(showMarkers){
			grid.markCell(firstCell,MazeGrid.MARKER2);
		}
		frontier.add(firstCell);
	}
	
    /**
       Generates the entire Maze in MazeGrid
    */
    public void generate(){
    	while(this.step());
    }
    
    /**
       Steps the maze generating algorithm once
       Best used for watching the maze being generating by calling
       MazeComponent.repaint() and Thread.sleep() between each step
       @return returns true if the generator can still step, false if the maze
       has stepped to completion
    */
    public boolean step(){
		System.out.print(frontier.size()+",");
    	//Step until frontier is empty = all cells cleared. 
		if(this.frontier.size()==0) return false;
		
		//Pick cell at random from frontier list
		Cell current = frontier.get(random.nextInt(frontier.size()));
		//Carve to a random cleared neighbor
		carveCell(current);
		//Add non-cleared neighbors to frontier
		addNeighbors(current);
		//Mark 
		if(showMarkers){
			grid.markCell(current,MazeGrid.MARKER3);
		}
		frontier.remove(current);
		
		return true;
    }
	/**
		Carves from given cell to one of its cleared neighbors
		(chosen at random)
		*/
	private void carveCell(Cell c){
		ArrayList<Cell> clearedNeighbors = findClearedNeighbors(c);
		grid.carvePath(c,clearedNeighbors.get(random.nextInt(clearedNeighbors.size())));
			
	}
	/**
		@param cell whose neighbors we are interested in
		@return list of cleared neighbors
		*/	
	private ArrayList<Cell> findClearedNeighbors(Cell c){
		ArrayList<Cell> clearedNeighbors = findNeighbors(c);
		ArrayList<Cell> returnList = new ArrayList<Cell>();
		for(Cell n: clearedNeighbors){
			if(grid.isVisited(n)){
				returnList.add(n);
			}
		}
		return returnList;
	}
	
	/**
		@param cell whose neighbors we are interested in
		@return list of neighbors
		*/
	private ArrayList<Cell> findNeighbors(Cell c){
		int row = c.row;
		int col = c.col;
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		ArrayList<Cell> returnList = new ArrayList<Cell>();
		neighbors.add(new Cell(row,col+1));
		neighbors.add(new Cell(row,col-1));
		neighbors.add(new Cell(row+1,col));
		neighbors.add(new Cell(row-1,col));
		
		for(Cell n: neighbors){
			if(grid.contains(n)){
				 returnList.add(n);
			}
		}
		return returnList;
	}
	
	/**
		Tests if neighbors to a cell aren't cleared and if they aren't 
		already in the frontier. If so, they are added to the frontier.
		@param cell whose neighbors are to be investigated
		*/
	
	public void addNeighbors(Cell c){
		ArrayList<Cell> neighbors = findNeighbors(c);
		
		for(Cell n: neighbors){
			if(!grid.isVisited(n)&&!frontier.contains(n)){
				if(showMarkers){
					grid.markCell(n,MazeGrid.MARKER1);
				}
				frontier.add(n);
			}
		}
		
	}
    
}