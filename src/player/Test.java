package player;

public class Test {
	public static void main(String[] args){
		MachinePlayer bob  = new MachinePlayer(1);
//		bob.forceMove(new Move(1,1));
//		bob.opponentMove(new Move(2,1));
//		bob.forceMove(new Move (2,2));
//		bob.opponentMove(new Move(3,2));
//		bob.forceMove(new Move (4,2));
//		bob.opponentMove(new Move(5,1));
//		bob.forceMove(new Move (5,3));
//		bob.opponentMove(new Move(6,3));
//		bob.forceMove(new Move (1,4));
//		bob.opponentMove(new Move(2,4));
//		
//		bob.forceMove(new Move (2,5));
//		bob.opponentMove(new Move(3,5));
//		bob.forceMove(new Move (4,5));
//		bob.opponentMove(new Move(5,5));
//		bob.forceMove(new Move (5,6));
//		bob.opponentMove(new Move(6,6));
//		bob.forceMove(new Move (6,1));
//		bob.opponentMove(new Move(4,0));
//		bob.forceMove(new Move(0,6));
//		bob.opponentMove(new Move(1,7));
		bob.forceMove(new Move(0,1));
		bob.opponentMove(new Move(1, 0));
		bob.forceMove(new Move(2,1));
		bob.opponentMove(new Move(1,2));
		bob.forceMove(new Move(4,1));
		bob.opponentMove(new Move(1,4));
		bob.forceMove(new Move(0,3));
		bob.opponentMove(new Move(3,0));
		bob.forceMove(new Move(2,3));
		bob.opponentMove(new Move(3,2));
		bob.forceMove(new Move(4,3));
		bob.opponentMove(new Move(3,4));
		bob.forceMove(new Move(0,5));
		bob.opponentMove(new Move(5,0));
		bob.forceMove(new Move(2,5));
		bob.opponentMove(new Move(5,2));
		bob.forceMove(new Move(4,5));
		bob.opponentMove(new Move(5,4));
		bob.forceMove(new Move(6,5));
		bob.opponentMove(new Move(5,6));
		
		System.out.println(bob.getBoard().toString());
		bob.chooseMove();
		System.out.println(bob.getBoard().toString());
	}
}
