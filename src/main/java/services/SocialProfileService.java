
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SocialProfileRepository;
import security.LoginService;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	@Autowired
	private SocialProfileRepository	socialProfileRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;


	public SocialProfile save(SocialProfile socialProfile) {
		return this.socialProfileRepository.save(socialProfile);
	}

	public SocialProfile create() {
		SocialProfile socialProfile = new SocialProfile();

		socialProfile.setName("");
		socialProfile.setNick("");
		socialProfile.setProfileLink("");

		return socialProfile;
	}

	public SocialProfile create(String nick, String name, String profileLink) {
		SocialProfile socialProfile = new SocialProfile();

		socialProfile.setName(name);
		socialProfile.setNick(nick);
		socialProfile.setProfileLink(profileLink);

		return socialProfile;
	}

	public List<SocialProfile> findAll() {
		return this.socialProfileRepository.findAll();
	}

	public SocialProfile findOne(int socialProfileId) {
		return this.socialProfileRepository.findOne(socialProfileId);
	}

	public void delete(SocialProfile socialProfile) {
		this.socialProfileRepository.delete(socialProfile);
	}

	public void deleteSocialProfile(SocialProfile socialProfile) {
		Actor logguedActor = this.actorService.getActorByUsername(LoginService.getPrincipal().getUsername());
		List<SocialProfile> socialProfiles = logguedActor.getSocialProfiles();

		socialProfiles.remove(socialProfile);
		logguedActor.setSocialProfiles(socialProfiles);
		this.actorService.save(logguedActor);
		this.socialProfileRepository.delete(socialProfile);
	}

	public SocialProfile reconstruct(SocialProfile socialProfile, BindingResult binding) {

		SocialProfile result;
		SocialProfile copy;
		//result = this.create();
		if (socialProfile.getId() == 0)
			result = socialProfile;
		else
			//copy = this.socialProfileRepository.findOne(socialProfile.getId());
			result = socialProfile;

		this.validator.validate(result, binding);
		return result;

	}
}
