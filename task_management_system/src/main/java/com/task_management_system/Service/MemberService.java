package com.task_management_system.Service;

import com.task_management_system.Entity.Member;
import com.task_management_system.Entity.Notification;
import com.task_management_system.Entity.Task;
import com.task_management_system.Enum.Status;
import com.task_management_system.Exception.MemberByIdNotFoundException;
import com.task_management_system.Exception.MemberByNameAlreadyExistException;
import com.task_management_system.Repository.AsMemberRepository;
import com.task_management_system.Request.Member.MemberCreateRequest;
import com.task_management_system.Request.Member.MemberUpdateRequest;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final AsMemberRepository memberRepository;

    public MemberService(AsMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Iterable<Member> list() {
        return this.memberRepository.findAll();
    }

    public Member create(MemberCreateRequest request) throws Exception {
        this.checkReceivedName(request.getName());

        Member member = Member.builder()
            .name(request.getName())
            .tasks(new ArrayList<>())
            .build()
        ;
        this.memberRepository.save(member);

        return member;
    }

    public Member read(UUID id) throws Exception {
        return this.memberRepository.findById(id).orElseThrow(() -> new MemberByIdNotFoundException(id));
    }

    public Member update(Member member, MemberUpdateRequest request) throws Exception {
        if (null != request.getName()) {
            this.checkReceivedName(request.getName(), member.getId());
        }

        member.setName(request.getName());

        return member;
    }

    public void delete(Member member) {
        this.memberRepository.delete(member);
    }

    public Map<String, Status> getTaskStatuesByMember(Member member) {
        return member.getTasks()
            .stream()
            .collect(Collectors.toMap(Task::getName, Task::getStatus))
        ;
    }

    public List<Notification> getNotificationsByMember(Member member) {
        return member.getNotifications()
            .stream()
            .filter(n -> !n.isRead())
            .toList()
        ;
    }

    private void checkReceivedName(String name) throws Exception {
        if (this.memberRepository.isExistByName(name)) {
            throw new MemberByNameAlreadyExistException(name);
        }
    }

    private void checkReceivedName(String name, UUID id) throws Exception {
        if (this.memberRepository.isExistByName(name, id)) {
            throw new MemberByNameAlreadyExistException(name);
        }
    }
}