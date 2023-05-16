package study.sns.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.sns.domain.dto.story.StoryEditRequest;
import study.sns.domain.enum_class.StoryScope;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Story {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private StoryScope scope;

    @OneToMany(mappedBy = "story", orphanRemoval = true)
    private List<UploadImage> uploadImages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    public void update(String newTitle, String newBody, LocalDate newDate, StoryScope newScope) {
        this.title = newTitle;
        this.body = newBody;
        this.date = newDate;
        this.scope = newScope;
    }

}
