package com.olegshan.sites;

import com.olegshan.parser.siteparsers.JobParser;

public abstract class JobSite {

    public abstract String name();

    public abstract String url();

    public String urlPrefix() {
        return "";
    }

    public String split() {
        return "";
    }

    public Holder jobBox() {
        return Holder.empty();
    }

    public Holder titleBox() {
        return Holder.empty();
    }

    public Holder company() {
        return Holder.empty();
    }

    public Holder description() {
        return Holder.empty();
    }

    public Holder date() {
        return Holder.empty();
    }

    public JobParser getParser() {
        return new JobParser(this);
    }

    public static class Holder {
        public String key;
        public String value;

        public static Holder of(String key, String value) {
            Holder holder = new Holder();
            holder.key = key;
            holder.value = value;

            return holder;
        }

        public static Holder empty() {
            return Holder.of("", "");
        }
    }
}
