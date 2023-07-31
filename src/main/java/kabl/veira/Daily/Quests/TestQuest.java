package kabl.veira.Daily.Quests;

import kabl.veira.Daily.DailyQuest;

public class TestQuest extends DailyQuest {
    public TestQuest(){
        this.id = 1;
        this.questTitle = "Test-Quest";
        this.questDescription = "Die ist eine Test-Quest, wenn du diese erhalten hast... Opfer";
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return this.questTitle;
    }

    @Override
    public String getDescription() {
        return this.questDescription;
    }
}
