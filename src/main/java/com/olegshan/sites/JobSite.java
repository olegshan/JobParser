package com.olegshan.sites;

import com.olegshan.parser.siteparsers.JobParser;

public interface JobSite {

	String name();

	String url();

	String urlPrefix();

	String split();

	Holder jobBox();

	Holder titleBox();

	Holder company();

	Holder description();

	Holder date();

	default JobParser getParser() {
		return new JobParser(this);
	}

	class Holder {
		public String key;
		public String value;

		public static Holder of(String key, String value) {
			Holder holder = new Holder();
			holder.key = key;
			holder.value = value;

			return holder;
		}
	}
}
