package algorithms;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultTeam {

    public ArrayList<Point> calculFVS( ArrayList<Point> points ) {

        return bbfAlgo( points );
    }

    /**
     * Compute a feedback vertex set
     * @param points vertices of the graph
     * @return vertex set
     */
    public ArrayList<Point> bbfAlgo( ArrayList<Point> points ) {
        ArrayList<Point> v = ( ArrayList<Point> ) points.clone();
        Map<Point , Double> w = new HashMap<Point , Double>(); // Map associate each vertixe with its Weight 
        Deque<Point> stack = new ArrayDeque<Point>();

        // Initialize F = {u∈V : w(u)=0},V =V −F
        for ( int i = 0 ; i < v.size() ; i++ )
            w.put( v.get( i ) , 1.0 );
        Set<Point> f = new HashSet<Point>();
        f.add( v.remove( 0 ) );

        // Cleanup g
        cleanUp( v );

        while ( !v.isEmpty() ) {
            
            ArrayList<Point> c = semiDisjoint( v );
            if ( c != null && !c.isEmpty() && c.size() != 1 ) { // g contains a semidisjoint cycle
                double lambda = min( c , w );
                for ( Point p : c )
                    w.put( p , w.get( p ) - lambda );
            }
            else { // g is clean and contains no semidisjoint cycle
                double lambda = min2( v , w );
                for ( Point p : v )
                    w.put( p , w.get( p ) - lambda * ( d( p , v ) - 1 ) );
            }

            for ( int i = 0 ; i < v.size() ; i++ ) {
                if ( w.get( v.get( i ) ) == 0 ) {
                    Point u = v.remove( i );
                    i--;
                    f.add( u );
                    stack.push( u );
                }
            }
            cleanUp( v );
        }
        while ( !stack.isEmpty() ) {
            Point u = stack.pop();
            ArrayList<Point> tempF = new ArrayList<Point>( f );
            tempF.remove( u );
            if ( Evaluation.isValide( points , tempF ) ) // u is redundant
                f.remove( u );
        }
        return new ArrayList<>( f );
    }

    /**
     * @param v vertices of a graph
     * @param weight of vertex
     * @return min{weight(u) : u∈v}
     */
    public double min( ArrayList<Point> v , Map<Point , Double> weight ) {
        double min = Double.MAX_VALUE;
        for ( Point p : v ) {
            double w = weight.get( p );
            if ( w < min )
                min = w;
        }
        return min;
    }

    /**
     * @param v vertices of a graph
     * @param weight of vertex
     * @return min{weight(u)/(d(u)−1) | u∈v}
     */
    public double min2( ArrayList<Point> v , Map<Point , Double> weight ) {
        double min = Double.MAX_VALUE;
        for ( Point p : v ) {
            double w = weight.get( p ) / ( d( p , v ) - 1 );
            if ( w < min )
                min = w;
        }
        return min;
    }

    /**
     * @param p vertex of a graph
     * @param vertices of the graph
     * @return degree of p in the graph
     */
    public int d( Point p , ArrayList<Point> vertices ) {
        return Evaluation.neighbor( p , vertices ).size();
    }

    /**
     * Clean up the graph. The result is that the graph doesnt contain any vertex of degree less than 2
     * @param v vertices of the graph
     */
    public void cleanUp( ArrayList<Point> v ) {
        boolean shouldContinue = true;
        while ( shouldContinue && !v.isEmpty() ) {
            for ( int i = 0 ; i < v.size() ; i++ )
                if ( d( v.get( i ) , v ) < 2 ) {
                    v.remove( i );
                    i--;
                    shouldContinue = true;
                }
                else
                    shouldContinue = false;
        }
    }

    /**
     * Find out a semiDisjoint cycle C.
     * C is semi disjoint if, for every vertex u of C 
     * d(u) = 2 with at most one exception.
     * @param vertices of the graph
     * @return a semidisjoint cycle if it exists. null otherwise
     */
    public ArrayList<Point> semiDisjoint( ArrayList<Point> vertices ) {
        // Get a cycle in the graph
        ArrayList<Point> cycle = getCycle( vertices );
    
        // Check wether cycle is semidisjoint or not
        int nbException = 0;
        for ( Point p : cycle ) {
            if ( d( p , vertices ) > 2 ) {
                nbException++;
                if ( nbException > 1 )
                    return null;
            }
        }
        return cycle;
    }

    /**
     * Find out a cycle in the graph. A recursive depth-first search algorithm is used.
     * @param vertices of the graph
     * @return a cycle in the graph
     */
    public ArrayList<Point> getCycle( ArrayList<Point> vertices ) {
        Set<Point> visited = new HashSet<Point>();
        Deque<Point> stack = new ArrayDeque<Point>(); // the cycle will be stored here
        dfs( vertices , vertices.get( 0 ) , new Point() , visited , stack );
        return new ArrayList<>( stack );
    }

    /**
     * Recursive method for Depth-first search
     * @param g vertices of the graph
     * @param v current vertex
     * @param father  of the current vertex 
     * @param visited : set of visited vertex
     * @param stack contains the cycle at the end if it exists
     * @return true if a cycle is detected, false otherwise
     */
    private boolean dfs( ArrayList<Point> g , Point v , Point father , Set<Point> visited , Deque<Point> stack ) {
        visited.add( v );
        stack.push( v );
        ArrayList<Point> children = Evaluation.neighbor( v , g );
        for ( Point w : children ) {
            if ( !father.equals( w ) ) {
                if ( !visited.contains( w ) ) {
                    boolean result = dfs( g , w , v , visited , stack );
                    if ( !result )
                        stack.pop();
                }
                else
                    return true;
            }
        }
        return false;
    }

}
