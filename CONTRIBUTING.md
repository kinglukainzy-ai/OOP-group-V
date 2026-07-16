# Contributing & Team Roles — OOP-group-V (Library Management System)

## Team Split

### Architecture Team (`model/`) — 6 files
| Member | Files |
|---|---|
| benjaminsiatey | `Book.java`, `PhysicalBook.java` |
| Bernice5307 | `EBook.java`, `Member.java` |
| biggystepizzy1z | `BorrowTransaction.java`, `Library.java` |

### Backend Team (`service/` + `util/`) — 8 files
| Member | Files |
|---|---|
| duodumichael73-art | `BookService.java`, `BookServiceImpl.java` |
| Femi099-source | `MemberService.java`, `MemberServiceImpl.java` |
| SedKing | `BorrowTransactionService.java`, `BorrowTransactionServiceImpl.java` |
| sysfade | `LibraryData.java`, `FileIOHelper.java` |

### Frontend Team (`ui/`) — 3 files
| Member | Files |
|---|---|
| Prodigalson | `MainFrame.java`, `BookPanel.java`, `BorrowTransactionPanel.java` |

### Additional Files Taken On by Prodigalson
These weren't originally assigned to anyone in the team split above. Prodigalson wrote them out of necessity while building the `ui/` layer, since the app couldn't be wired together without them.

| File | Why it was needed |
|---|---|
| `util/ServiceRegistry.java` | No one owned the util class the GUI needs to reach service instances (referenced in README §10, never assigned). |
| `LMSApplication.java` | App entry point referenced in README §10 as the place `ServiceRegistry.initialize()` gets called — didn't exist anywhere in the repo. |

> Note: these touch shared/unassigned ground, not another member's owned file. If your assigned file needs a method that doesn't exist yet (e.g. `BookService` is missing an `updateBook()`), don't silently add it to someone else's file — open a separate PR, tag the file's owner as reviewer, and flag it in the group chat.

---

## Build Order

1. **Architecture team merges first** — `model/` classes and field signatures must land on `main` before other teams branch off.
2. **Backend team** builds against merged `model/` classes.
3. **Frontend team** builds against merged `service/` interfaces.

Branching before the layer you depend on is merged will cause avoidable conflicts.

---

## Git Workflow

1. Create a branch for your task:
```bash
   git checkout -b feat-<yourname>-<package>
```
2. Write your file(s).
3. Stage and commit:
```bash
   git add .
   git commit -m "Implement <ClassName>"
```
4. Push your branch:
```bash
   git push origin feat-<yourname>-<package>
```
5. Open a Pull Request into `main`.
6. Wait for review and approval — **at least one other team member must approve** before merge.

---

## Branch Rules

- Never commit directly to `main`.
- One feature branch per task — keep branches small and focused.
- PR required, 1 approval minimum.
- Only **Prodigalson** (repo admin) can merge into `main`.