
package checkers; // Ten pakiet jest wymagany - nie usuwaj go
public class EvaluatePosition // Ta klasa jest wymagana - nie usuwaj jej
{
	static private final int WIN=Integer.MAX_VALUE/2;
	static private final int LOSE=Integer.MIN_VALUE/2;
	static private boolean _color; // To pole jest wymagane - nie usuwaj go
	// Pole przechowuje informację, które pionki należą do gracza bieżącego. Możliwe wartości:
	// 	true - jeżeli górne (żółte) pionki należą do gracza bieżącego
	// 	false - jeżeli dolne (czerwone) pionki należą do gracza bieżącego
	static public void changeColor(boolean color) // Ta metoda jest wymagana - nie zmieniaj jej
	{
		_color=color;
	}
	static public boolean getColor() // Ta metoda jest wymagana - nie zmieniaj jej
	{
		return _color;
	}
	// Metoda określa, które pionki należą do gracza bieżącego. Wartości zwracane:
	// 	true - jeżeli górne (żółte) pionki należą do gracza bieżącego
	// 	false - jeżeli dolne (czerwone) pionki należą do gracza bieżącego
	static public int determine_value_to_zone(int i, int j, int size, int part)
	{
		for (int k = 0; k  < part; k++) 
		{
			if ((i == 0 + k ) || (j == 0 + k) || (i == size-1 - k) || (j == size-1 - k)) 
			{
				return (part - 1 - k);
			}	
		}
		return 0;
	}

	static public int determine_value_to_lvl(boolean color, int i, int size)
	{
		int site = 0;
		int number_of_level = (size + 1) / 4;
		if (color) 
		{
			site = 0;
		}
		else
		{
			site = 3;
		}
		for (int k = 0; k<4 ; k++) 
		{
			if ((i >= (number_of_level - 2)) || (i < number_of_level)) {
				int tmp = site - k;
				int value = tmp * tmp;
				if (value == 9) value *= 2; 
				return value;
			}
			number_of_level += 2;
		}
		return 0;
	}

	static public boolean in_board(int i, int j, int size)
	{
		if (i >= 0 && i < size && j >= 0 && j < size) {
			return true;
		}
		return false;
	}

	// static public int kill(int i, int j, boolean color, int size, AIBoard board)
	// {
	// 	int sign = 0;
	// 	if (color) {
	// 		sign = 1;
	// 	}
	// 	else
	// 	{
	// 		sign = -1;
	// 	}
	// 	if (in_board(i + sign, j + 1, size)) {
	// 		if ((board._board[i + sign ][j + 1].white != color) && ( (!in_board(i - sign, j - 1, size)) || (!board._board[i - sign][j - 1].empty))) {
	// 			if ((!in_board(i + sign, j - 1, size) || board._board[i + sign][j - 1].empty) && in_board(i + 2*sign, j + 2, size)) {
	// 				if (board._board[i + 2*sign][j + 2].empty) return 30;
	// 			}
	// 		}
	// 	}
	// 	else if (in_board(i + sign, j - 1, size)) {
	// 		if ((board._board[i + sign][j - 1].white != color) && ((!in_board(i - sign, j + 1, size)) || (!board._board[i - sign][j + 1].empty))) {
	// 			if ((!in_board(i + sign, j + 1, size) || board._board[i + sign][j + 1].empty) &&in_board(i + 2*sign, j - 2, size)) {
	// 				if (board._board[i + 2*sign][j - 2].empty) return 30;
	// 			}
	// 		}
	// 	}
	// 	return 0;
	// }

	static public int evaluatePosition(AIBoard board) // Ta metoda jest wymagana. Jest to główna metoda heurystyki - umieść swój kod tutaj
	{
		int myRating=0;
		int opponentsRating=0;
		int size=board.getSize();
		int value_to_zone = 0;
		int value_to_lvl = 0;
		int part = (size+1) / 2;
		int value_for_kill = 0;
		int kings = 0;
		int pawns = 0;
		for (int i=0;i<size;i++)
		{
			for (int j=(i+1)%2;j<size;j+=2)
			{
				if (!board._board[i][j].empty) // pole nie jest puste
				{	
					value_to_zone = determine_value_to_zone(i, j, size, part);
					if (board._board[i][j].white==getColor()) // to jest moj pionek
					{
						if (board._board[i][j].king)
						{
							myRating+=25; // to jest moja damka
							kings += 1;
							value_to_lvl = determine_value_to_lvl(!getColor(), i, size);
						}
						else 
						{
							myRating+=5; // to jest moj pionek
							pawns += 1; 
							value_to_lvl = determine_value_to_lvl(getColor(), i, size);
							if (value_to_lvl == 0) value_to_lvl += 2;
							else if (value_to_lvl == 1) value_to_lvl += 4;
							// value_for_kill = kill(i, j, getColor(), size, board);
							// myRating += value_for_kill;
						}
						myRating += value_to_lvl;
						myRating += 3*value_to_zone;
						
					}
					else
					{
						if (board._board[i][j].king)
						{ 
							kings += 1;
							opponentsRating+=25; // to jest damka przeciwnika
							value_to_lvl = determine_value_to_lvl(getColor(), i, size);
						}
						else 
						{		
							pawns += 1;
							opponentsRating+=5;
							value_to_lvl = determine_value_to_lvl(!getColor(), i, size);
							if (value_to_lvl == 0) value_to_lvl += 2;
							else if (value_to_lvl == 1) value_to_lvl += 4;	
							// value_for_kill = kill(i, j, !getColor(), size, board);
							// opponentsRating += value_for_kill;
						}
						opponentsRating += value_to_lvl;
						opponentsRating += 3*value_to_zone;
					}
				}
			}
		}
		//Judge.updateLog("Tutaj wpisz swoją wiadomość - zobaczysz ją w oknie log\n");
		if (myRating==0) return LOSE; // przegrana
		else if (opponentsRating==0) return WIN; // wygrana
		else return myRating-opponentsRating;
	}
}
