package com.library.service;

import com.library.exception.MemberNotFoundException;
import com.library.model.Member;
import com.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(String memberId) {
        return memberRepository.findById(memberId);
    }

    public List<Member> getMembersWithBorrowedBooks() {
        return memberRepository.findMembersWithBorrowedBooks();
    }

    public void deleteMember(String memberId) throws MemberNotFoundException {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("Member with ID " + memberId + " not found");
        }
        memberRepository.deleteById(memberId);
    }


}
