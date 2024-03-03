package Model;

import Controller.ManageQuestionsController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SystemData implements QuestionObserver {
    private static SystemData instance;
    private final HashMap<Difficulty, ArrayList<Question>> questions;

    private SystemData() {
        questions = new HashMap<>();
    }

    public static SystemData getInstance() {
        if (instance == null)
            instance = new SystemData();
        return instance;
    }

    public boolean loadQuestions() {
        JSONParser parser = new JSONParser();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/Model/questions_scheme.json")))) {
            Object obj = parser.parse(reader);
            JSONObject jo = (JSONObject) obj;
            JSONArray questionsArray = (JSONArray) jo.get("questions");

            for (Object questionObj : questionsArray) {
                JSONObject q = (JSONObject) questionObj;
                String text = (String) q.get("question");
                JSONArray answersArray = (JSONArray) q.get("answers");
                String answer1 = (String) answersArray.get(0);
                String answer2 = (String) answersArray.get(1);
                String answer3 = (String) answersArray.get(2);
                String answer4 = (String) answersArray.get(3);
                String correctAnswer = (String) q.get("correct_ans");
                int difficulty = Integer.parseInt(q.get("difficulty").toString());

                Difficulty enumDifficulty = getQuestionDifficulty(difficulty);

                Question questionToAdd = new Question(text, answer1, answer2, answer3, answer4, correctAnswer, enumDifficulty);
                questions.computeIfAbsent(enumDifficulty, k -> new ArrayList<>()).add(questionToAdd);

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Difficulty getQuestionDifficulty(int difficulty) {
        switch (difficulty) {
            case 1:
                return Difficulty.EASY;
            case 2:
                return Difficulty.MEDIUM;
            case 3:
                return Difficulty.HARD;
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + difficulty);
        }
    }

    public void saveQuestions() {
        try {
            JSONArray JSONQuestions = new JSONArray();

            for (ArrayList<Question> list : questions.values()) {
                if (list == null)
                    continue;

                for (Question q : list) {
                    JSONObject questionObj = new JSONObject();
                    questionObj.put("question", q.getText());

                    JSONArray answersArray = new JSONArray();
                    answersArray.add(q.getAnswer1());
                    answersArray.add(q.getAnswer2());
                    answersArray.add(q.getAnswer3());
                    answersArray.add(q.getAnswer4());

                    questionObj.put("answers", answersArray);
                    questionObj.put("correct_ans", q.getCorrectAnswer());
                    questionObj.put("difficulty", String.valueOf(q.getLevel().ordinal() + 1));

                    JSONQuestions.add(questionObj);
                }
            }

            JSONObject toWrite = new JSONObject();
            toWrite.put("questions", JSONQuestions);

            // Specify the full path to the JSON file
            FileWriter file = new FileWriter("src/Model/questions_scheme.json");
            file.write(toWrite.toJSONString());
            file.flush();
            System.out.println("JSON Question was saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public HashMap<Difficulty, ArrayList<Question>> getQuestions() {
        return questions;
    }

    public List<Question> getAllQuestions() {
        List<Question> allQuestions = new ArrayList<>();
        for (ArrayList<Question> list : questions.values()) {
            if (list != null) {
                allQuestions.addAll(list);
            }
        }
        return allQuestions;
    }

    public List<Question> getAllQuestionsSortedById() {
        List<Question> allQuestions = getAllQuestions();
        allQuestions.sort(Comparator.comparingInt(Question::getQuestionId));
        return allQuestions;
    }

    public void deleteQuestion(Question question) {
        for (ArrayList<Question> list : questions.values()) {
            if (list != null) {
                list.removeIf(q -> q.equals(question));
            }
        }
    }

    @Override
    public void onQuestionAdded(Question question) {

    }

    @Override
    public void onQuestionEdited(Question oldQuestion, Question newQuestion) {

    }

    @Override
    public void onQuestionDeleted(Question deletedQuestion) {
        // Implement logic to handle deletion of questions
        // Remove the deleted question from the HashMap and save the changes to the JSON file
        for (ArrayList<Question> list : questions.values()) {
            if (list != null) {
                list.removeIf(q -> q.equals(deletedQuestion));
            }
        }
        saveQuestions();
    }

    public void registerObserver(ManageQuestionsController manageQuestionsController) {
    }

    public boolean addQuestion(Question newQuestion) {
        // Get the difficulty of the new question
        Difficulty difficulty = newQuestion.getLevel();

        // Get the list of questions for the given difficulty level
        ArrayList<Question> questionList = questions.getOrDefault(difficulty, new ArrayList<>());

        // Add the new question to the list
        questionList.add(newQuestion);

        // Update the HashMap with the modified list
        questions.put(difficulty, questionList);

        // Save the updated questions to the JSON file
        saveQuestions();

        // Return true to indicate that the question was successfully added
        return true;
    }
    public boolean editQuestion(Question editedQuestion) {
        // Get the difficulty of the edited question
        Difficulty difficulty = editedQuestion.getLevel();

        // Get the list of questions for the given difficulty level
        ArrayList<Question> questionList = questions.getOrDefault(difficulty, new ArrayList<>());

        // Find the index of the edited question in the list
        int index = -1;
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).getQuestionId() == editedQuestion.getQuestionId()) {
                index = i;
                break;
            }
        }

        // If the question is found, replace it with the edited question
        if (index != -1) {
            questionList.set(index, editedQuestion);

            // Update the HashMap with the modified list
            questions.put(difficulty, questionList);

            // Save the updated questions to the JSON file
            saveQuestions();

            // Return true to indicate that the question was successfully edited
            return true;
        } else {
            // If the question is not found, return false
            return false;
        }
    }

}
