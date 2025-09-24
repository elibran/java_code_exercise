package com.library.controller;

import com.library.model.Member;
import com.library.service.LibraryService;
import com.library.exception.MemberNotFoundException;
import com.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMemberById(@PathVariable String memberId) {
        Optional<Member> member = memberService.getMemberById(memberId);
        return member.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/with-borrowed-books")
    public ResponseEntity<List<Member>> getMembersWithBorrowedBooks() {
        List<Member> members = memberService.getMembersWithBorrowedBooks();
        return ResponseEntity.ok(members);
    }

    @PostMapping
    public ResponseEntity<Member> addMember(@Valid @RequestBody Member member) {
        Member savedMember = memberService.addMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<Member> updateMember(@PathVariable String memberId, @Valid @RequestBody Member member) {
        member.setMemberId(memberId);
        Member updatedMember = memberService.addMember(member);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable String memberId) {
        try {
            memberService.deleteMember(memberId);
            return ResponseEntity.noContent().build();
        } catch (MemberNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}