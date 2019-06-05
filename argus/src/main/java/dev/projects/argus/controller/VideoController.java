package dev.projects.argus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import dev.projects.argus.model.Videos;
import dev.projects.argus.repository.VideosRepository;

@Controller
public class VideoController {
	
	@Autowired
	private VideosRepository videos;
	
	@GetMapping("/videos")
	public ModelAndView videos() {
		ModelAndView mav = new ModelAndView("/videos");
		Videos stapple = videos.findById(1).orElse(null);
		List<Videos> all = videos.findAllVideos();
		mav.addObject("stapple", stapple);
		mav.addObject("videos", all);
		return mav;
	}
	
	@GetMapping("/videos/{tag}")
	public ModelAndView searchvideos(@PathVariable("tag") String tag) {
		ModelAndView mav;
		if(tag == "")
			return new ModelAndView("redirect:/cliente/videos");
		else {
			mav = new ModelAndView("cliente/searchVideos");
			mav.addObject("tag", tag);
		}
		List<Videos> vids = videos.findByTag(tag);
		mav.addObject("videos", vids);
		return mav;
	}
}
