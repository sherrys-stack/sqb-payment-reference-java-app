package com.example.sqb.bootstrap.controller;

import com.example.sqb.adapter.terminal.SqbActivateAdapter;
import com.example.sqb.adapter.terminal.SqbCheckinAdapter;
import com.example.sqb.protocol.dto.terminal.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/terminal")
public class TerminalController {
    private final SqbActivateAdapter activateAdapter;
    private final SqbCheckinAdapter checkinAdapter;

    public TerminalController(SqbActivateAdapter activateAdapter, SqbCheckinAdapter checkinAdapter) {
        this.activateAdapter = activateAdapter;
        this.checkinAdapter = checkinAdapter;
    }

    @PostMapping("/activate")
    public ActivateResponse activate(@RequestBody ActivateRequest request) {
        return activateAdapter.activate(request);
    }

    @PostMapping("/checkin")
    public CheckinResponse checkin(@RequestBody CheckinRequest request) {
        return checkinAdapter.checkin(request);
    }
}
