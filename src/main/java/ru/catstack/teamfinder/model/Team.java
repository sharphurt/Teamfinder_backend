package ru.catstack.teamfinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import ru.catstack.teamfinder.model.audit.DateAudit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teams")
@Indexed
public class Team extends DateAudit {
    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "pic")
    private String pic;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "teams_members",
            joinColumns = {@JoinColumn(name = "team_id", referencedColumnName = "team_id")},
            inverseJoinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")})
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Member> members;

    @JsonIgnore
    @Column(name = "tags_list")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String tagsList = "";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "teams_tags",
            joinColumns = {@JoinColumn(name = "team_id", referencedColumnName = "team_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Tag> tags;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "creator_member_id", referencedColumnName = "member_id")
    private Member creator;

    public Team(String name, String description, Member creator, String picCode) {
        this.name = name;
        this.description = description;
        this.members = new ArrayList<>();
        this.creator = creator;
        this.pic = picCode;
        this.status = Status.ACTIVE;
    }

    public Team() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Member> getMembers() {
        return members;
    }

    public String getPic() {
        return pic;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Member getCreator() {
        return creator;
    }

    public void addMember(Member member) {
        this.members.add(member);
    }

    public void removeMember(Member member) {
        this.members.remove(member);
    }

    public void addTag(String tag) {
        this.tags.add(new Tag(tag));
        this.tagsList = getTagsAsString();
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        this.tagsList = getTagsAsString();
    }

    private String getTagsAsString() {
        var result = new String[this.tags.size()];
        var tagsArr = new ArrayList<>(this.tags);
        for (var i = 0; i < this.tags.size(); i++)
            result[i] = tagsArr.get(i).getTag();
        return Arrays.toString(result);
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public String getTagsList() {
        return tagsList;
    }
}
