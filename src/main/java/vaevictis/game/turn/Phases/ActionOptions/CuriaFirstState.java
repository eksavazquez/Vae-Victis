package vaevictis.game.turn.Phases.ActionOptions;

import java.util.List;

import javax.persistence.Entity;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.Turn;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class CuriaFirstState extends PhaseState {

    public static final String phaseName = "CuriaFirst";

    public Game game;
    public Player player;

    public CuriaFirstState(Game game, Player player){
        this.game = game;
        this.player = player;
        super.setName(phaseName);
    }
    public CuriaFirstState(){}
    

    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
        List<Integer> electedActions = this.game.getPlayersTurn().getSecondAction().toArray();

        Integer option = electedActions.get(2);
        if(option == 2) {
            phase.setState(new CuriaSecondState(this.game, this.player));
        } else {
            phase.setState(new EndTurnState(this.game, this.player));
        }
    }

    @Override
    public void prev(Phase phase) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printPhase() {
        // TODO Auto-generated method stub
        System.out.println(phaseName);
        
    }

    @Override
    public void phaseHandle(GameService gameService, TurnRequest turnRequest, TurnOutput turnOutput) {
        // TODO Auto-generated method stub
        Integer selectedCuria = turnRequest.selectedCuria;

        
        Boolean plusOrMinus=selectedCuria%2==0;
        int section=0;
        if(selectedCuria<=2){
            section=1;
        }else if(selectedCuria<=4){
            section=2;
        }else{
            section=3;
        }

        turnOutput.setSectionCuria(section);
        turnOutput.setPlusOrMinus(plusOrMinus);

        gameService.saveActionCuria(this.game, plusOrMinus, section);

        Turn turn = game.getPlayersTurn();
        PhaseState phaseState = gameService.phaseStateService.create(this);

        Phase phase = turn.getPhase();

        phase.setState(phaseState);

        gameService.phaseService.save(phase);

        game.setPlayersTurn(turn);
        gameService.saveGame(game);

        phase.nextState();

        turnOutput.setPhase(phase);
        turnOutput.setTurn(turn);
        turnOutput.setPlayersTurn(player);
        
    }

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }
    
}
