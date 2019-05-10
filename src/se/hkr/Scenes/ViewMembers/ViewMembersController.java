package se.hkr.Scenes.ViewMembers;

import se.hkr.Model.User.Member;
import se.hkr.Scenes.ReadController;

public class ViewMembersController implements ReadController<Member> {

    @Override
    public boolean filter(Member model) {
        return false;
    }
    
    @Override
    public void search() {

    }
}
