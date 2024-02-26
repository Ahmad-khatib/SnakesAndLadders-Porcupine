package JUnits;

import Model.Difficulty;
import Model.Question;
import Model.SystemData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class LoadingQuestionsTest {
    @Test
    public void LoadQuestionsTest() {
        SystemData.getInstance().loadQuestions();
        HashMap<Difficulty, ArrayList<Question>> questions = SystemData.getInstance().getQuestions();
        SystemData.getInstance().loadQuestions();
        HashMap<Difficulty, ArrayList<Question>> questions2 = SystemData.getInstance().getQuestions();

        assertTrue("Successful Load Question", questions.equals(questions2));
    }
}