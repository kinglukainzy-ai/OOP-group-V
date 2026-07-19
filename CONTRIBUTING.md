# Contributing & Team Roles — OOP-group-V (Library Management System)

## Team Roster

| Name | Username | Role/Team | Branch | Status |
|---|---|---|---|---|
| Benjamin Siatey | benjaminsiatey | Architecture — `Book.java`, `PhysicalBook.java` | `feat-benjamin` | ⚠️ Attempted — branch exists but incompatible with current model design, never merged |
| Bernice Boadi Kodua | Bernice5307 | Architecture — `EBook.java`, `Member.java` | `feat-Bernice5307-model` | ✅ Done — merged |
| Michael Darko Duodu | duodumichael73-art | Backend — `BookService.java`, `BookServiceImpl.java` | `feature/book-service` | ✅ Done — merged |
| Newton Mensah Holy | Femi099-source | Backend — `MemberService.java`, `MemberServiceImpl.java` | `Femi099-source-patch-1` | ✅ Done — merged |
| Dennis Ephraim Seblanu | SedKing | Backend — `BorrowTransactionService.java`, `BorrowTransactionServiceImpl.java` | `feat-SedKing-BorrowTransactionService` | ✅ Done — merged |
| Terrence Ansa-Sasraku | sysfade | Backend — `LibraryData.java`, `FileIOHelper.java` | `TERRENCE_ANSA_UML` | ✅ Done — merged |
| (you) | Prodigalson | Frontend — `MainFrame.java`, `BookPanel.java`, `BorrowTransactionPanel.java` | `feat-prodigalson-ui` | ✅ Done — merged, plus additional unassigned files (see below) |

> Note on `Book.java`/`PhysicalBook.java` and `BorrowTransaction.java`/`Library.java`: these files exist and work on `main`, but were written by Prodigalson to keep the app buildable — not by the originally assigned owners. Flagged here for transparency, not to reassign credit.

## Unparticipating Members

| Name | Username | Assigned Role | Status |
|---|---|---|---|
| Solomon Adjinayie | biggystepizzy1z | Architecture — `BorrowTransaction.java`, `Library.java` | 🔴 Zero commits, no branch activity |

---

### Additional Files Taken On by Prodigalson
Not originally assigned to anyone. Written out of necessity while building the `ui/` layer, since the app couldn't be wired together without them.

| File | Why it was needed |
|---|---|
| `util/ServiceRegistry.java` | No one owned the util class the GUI needs to reach service instances (referenced in README §10, never assigned). |
| `LMSApplication.java` | App entry point referenced in README §10 — didn't exist anywhere in the repo. |

> If your assigned file needs a method that doesn't exist yet (e.g. `BookService` missing an `updateBook()`), don't silently add it to someone else's file — open a separate PR, tag the owner as reviewer, and flag it in the group chat.

---

## Build Order

1. **Architecture team merges first** — `model/` classes and field signatures must land on `main` before other teams branch off.
2. **Backend team** builds against merged `model/` classes.
3. **Frontend team** builds against merged `service/` interfaces.

Branching before the layer you depend on is merged causes avoidable conflicts.

---

## Git Workflow

1. Create a branch: `git checkout -b feat-<yourname>-<package>`
2. Write your file(s).
3. Stage and commit: `git add . && git commit -m "Implement <ClassName>"`
4. Push: `git push origin feat-<yourname>-<package>`
5. Open a Pull Request into `main`.
6. Wait for review and approval — **at least one other team member must approve** before merge.

---

## Branch Rules

- Never commit directly to `main`.
- One feature branch per task.
- PR required, 1 approval minimum.
- Only **Prodigalson** (repo admin) can merge into `main`.
